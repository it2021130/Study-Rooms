package gr.hua.dit.studyrooms.core.service.impl;

import gr.hua.dit.studyrooms.core.model.*;
import gr.hua.dit.studyrooms.core.repository.PersonRepository;
import gr.hua.dit.studyrooms.core.repository.ReservationRepository;
import gr.hua.dit.studyrooms.core.repository.StudyRoomRepository;
import gr.hua.dit.studyrooms.core.security.CurrentUser;
import gr.hua.dit.studyrooms.core.model.ReservationStatus;
import gr.hua.dit.studyrooms.core.security.CurrentUserProvider;
import gr.hua.dit.studyrooms.core.service.ReservationService;
import gr.hua.dit.studyrooms.core.service.mapper.ReservationMapper;
import gr.hua.dit.studyrooms.core.service.model.CancelReservationRequest;
import gr.hua.dit.studyrooms.core.service.model.CreateReservationRequest;
import gr.hua.dit.studyrooms.core.service.model.ReservationView;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReservationServiceImpl implements ReservationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private static final int MAX_RESERVATIONS_PER_DAY = 4;
    private static final Set<ReservationStatus> ACTIVE = Set.of(ReservationStatus.ACTIVE);
    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final PersonRepository personRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final CurrentUserProvider currentUserProvider;

    public ReservationServiceImpl(
            final ReservationMapper reservationMapper,
            final ReservationRepository reservationRepository,
            final CurrentUserProvider currentUserProvider,
            PersonRepository personRepository,
            StudyRoomRepository studyRoomRepository
    ) {
        if (reservationMapper == null) throw new NullPointerException();
        if (reservationRepository == null) throw new NullPointerException();
        if (personRepository == null) throw new NullPointerException();
        if (currentUserProvider == null) throw new NullPointerException();
        if (studyRoomRepository == null) throw new NullPointerException();

        this.reservationMapper = reservationMapper;
        this.reservationRepository = reservationRepository;
        this.personRepository = personRepository;
        this.currentUserProvider = currentUserProvider;
        this.studyRoomRepository = studyRoomRepository;
    }

    @Override
    public List<ReservationView> getReservationHistoryOfCurrentUser() {

        final CurrentUser currentUser = currentUserProvider.requireCurrentUser();
        final List<Reservation> reservationListList;
        if (currentUser.type() != PersonType.STUDENT) {
            throw new SecurityException("unsupported PersonType");
        }

        long studentId = currentUser.id();

        reservationListList =
                reservationRepository.findByStudentIdOrderByDateDesc(studentId);

        return reservationListList.stream()
                .map(reservation -> {
                    if (reservation.getStudent().getId() != studentId) {
                        throw new SecurityException("User cannot access this reservation");
                    }
                    return reservationMapper.convertReservationToReservationView(reservation);
                })
                .toList();
    }

    @Override
    public List<ReservationView> getReservationsOfCurrentUser() {
        final CurrentUser currentUser = this.currentUserProvider.requireCurrentUser();
        long studentId = currentUser.id();
        final List<Reservation> reservationList;
        LocalDate today = LocalDate.now();
        if (currentUser.type() == PersonType.STUDENT) {
            reservationList = this.reservationRepository.findByStudentIdAndStatusAndDateGreaterThanEqualOrderByDateAsc(
                    studentId,
                    ReservationStatus.ACTIVE,
                    today
            );
        }else {
            throw new SecurityException("unsupported PersonType");
        }
        return reservationList.stream()
                .map(this.reservationMapper::convertReservationToReservationView)
                .toList();
    }

    @Transactional
    @Override
    public ReservationView bookReservation(CreateReservationRequest req) {
        if (req == null) throw new NullPointerException();
        final CurrentUser currentUser = this.currentUserProvider.requireCurrentUser();
        if (currentUser.type() != PersonType.STUDENT) {
            throw new SecurityException("Student type required");
        }

        final long studentId = currentUser.id();
        final long roomId = req.roomId();
        final LocalDate date = req.date();
        LocalDate today = LocalDate.now();

        final Person student = this.personRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated student  not found"));

        if (student == null || student.getType() != PersonType.STUDENT) {
            throw new IllegalArgumentException("studentId must refer to a STUDENT");
        }

        final StudyRoom room = studyRoomRepository.findById(req.roomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        // Security
        // --------------------------------------------------
        if (date.isBefore(today)) {
            throw new IllegalArgumentException("Reservation date must be today or later");
        }

        // RULE 1: max 4 per day
        final long activeCount = this.reservationRepository.countByStudentIdAndDateAndStatusIn(studentId,date, ACTIVE);
        if (activeCount >= MAX_RESERVATIONS_PER_DAY ) {
            throw new RuntimeException("You already have 4 reservations on this date.");
        }
        List<Reservation> activeReservations =
                reservationRepository.findByRoomIdAndDateAndStatus(
                        roomId, date, ReservationStatus.ACTIVE
                );

        int seatsAlreadyBooked = activeReservations.stream()
                .mapToInt(Reservation::getSeatsReserved)
                .sum();

        if (seatsAlreadyBooked + req.seatsRequested() > room.getCapacity()) {
            throw new IllegalStateException("Not enough seats available.");
        }

        // Rule 3: cannot book outside room hours
        if (room.getOpeningTime() == null || room.getClosingTime() == null) {
            throw new RuntimeException("This room has no valid opening/closing hours");
        }

        // Create reservation
        Reservation r = new Reservation();
                r.setStudent(student);
                r.setRoom(room);
                r.setDate(date);
                r.setStartTime(room.getOpeningTime());
                r.setEndTime(room.getClosingTime());
                r.setStatus(ReservationStatus.ACTIVE);
                r.setSeatsReserved(req.seatsRequested());
        r = this.reservationRepository.save(r);

        final ReservationView reservationView = this.reservationMapper.convertReservationToReservationView(r);
        return reservationView;
    }


    @Override
    public ReservationView cancelReservation(CancelReservationRequest req) {
        if (req == null) throw new NullPointerException();

        final long reservationid = req.reservationId();

        final Reservation reservation = this.reservationRepository.findById(reservationid)
                .orElseThrow(() -> new IllegalArgumentException("Reservation does not exist"));

        final long studentId = reservation.getStudent().getId();
        final CurrentUser currentUser = this.currentUserProvider.requireCurrentUser();

        if (currentUser.type() != PersonType.STUDENT) {
            throw new SecurityException("Student role/type required");
        }
        if (currentUser.id() != studentId) {
            throw new SecurityException("Authenticated student does not match the ticket's studentId");
        }
        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalArgumentException("Only ACTIVE reservations can be cancelled");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(Instant.now());
        final Reservation savedReservation = this.reservationRepository.save(reservation);
        final ReservationView reservationView = this.reservationMapper.convertReservationToReservationView(savedReservation);

        return reservationView;
    }
    @Override
    public List<ReservationView> getAllActiveReservationsForStaff() {

        final CurrentUser currentUser = currentUserProvider.requireCurrentUser();
        if (currentUser.type() != PersonType.STAFF) {
            throw new SecurityException("Staff required");
        }

        List<Reservation> reservations =
                reservationRepository.findByStatusOrderByDateAsc(ReservationStatus.ACTIVE);

        return reservations.stream()
                .map(reservationMapper::convertReservationToReservationView)
                .toList();
    }

    @Override
    public ReservationView cancelReservationByStaff(CancelReservationRequest req) {
        if (req == null) throw new NullPointerException();

        final CurrentUser currentUser = this.currentUserProvider.requireCurrentUser();


        if (currentUser.type() != PersonType.STAFF) {
            throw new SecurityException("Staff role/type required");
        }

        final long reservationId = req.reservationId();

        final Reservation reservation = this.reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation does not exist"));

        // Επιτρέπουμε ακύρωση μόνο ACTIVE κρατήσεων
        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalArgumentException("Only ACTIVE reservations can be cancelled by staff");
        }

        reservation.setStatus(ReservationStatus.CANCELLED_BY_STAFF);
        reservation.setCancelledAt(Instant.now());

        final Reservation savedReservation = this.reservationRepository.save(reservation);
        final ReservationView reservationView = this.reservationMapper.convertReservationToReservationView(savedReservation);

        return reservationView;
    }
}

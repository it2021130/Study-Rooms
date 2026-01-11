package gr.hua.dit.studyrooms.core.service.impl;

import gr.hua.dit.studyrooms.core.model.Reservation;
import gr.hua.dit.studyrooms.core.model.ReservationStatus;
import gr.hua.dit.studyrooms.core.repository.StudyRoomRepository;
import gr.hua.dit.studyrooms.core.repository.ReservationRepository;
import gr.hua.dit.studyrooms.core.service.AvailabilityService;
import gr.hua.dit.studyrooms.core.service.mapper.AvailabilityMapper;
import gr.hua.dit.studyrooms.core.service.model.RoomAvailabilityView;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final StudyRoomRepository studyRoomRepository;
    private final ReservationRepository reservationRepository;

    private static final Set<ReservationStatus> ACTIVE =
            Set.of(ReservationStatus.ACTIVE);

    private final AvailabilityMapper availabilityMapper;

    public AvailabilityServiceImpl(
            StudyRoomRepository studyRoomRepository,
            ReservationRepository reservationRepository,
            AvailabilityMapper availabilityMapper
    ) {
        this.studyRoomRepository = studyRoomRepository;
        this.reservationRepository = reservationRepository;
        this.availabilityMapper = availabilityMapper;
    }

    @Override
    public List<RoomAvailabilityView> getAvailabilityForDate(LocalDate date) {

        return studyRoomRepository.findAll()
                .stream()
                .filter(room -> room.getCapacity() > 0)
                .map(room -> {

                    List<Reservation> activeReservations =
                            reservationRepository.findByRoomIdAndDateAndStatus(
                                    room.getId(), date, ReservationStatus.ACTIVE
                            );

                    int seatsAlreadyBooked = activeReservations.stream()
                            .mapToInt(Reservation::getSeatsReserved)
                            .sum();

                    int available = Math.max(room.getCapacity() - seatsAlreadyBooked, 0);

                    return availabilityMapper.toView(room, date, available);
                })
                .toList();
    }
}



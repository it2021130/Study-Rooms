package gr.hua.dit.studyrooms.core.service.mapper;

import gr.hua.dit.studyrooms.core.model.Reservation;

import gr.hua.dit.studyrooms.core.service.model.ReservationView;

import org.springframework.stereotype.Component;
/**
 * Mapper for converting {@link Reservation} to {@link ReservationView}.
 */

@Component
public class ReservationMapper {

    private final PersonMapper personMapper;
    private final StudyRoomMapper studyRoomMapper;

    public ReservationMapper(PersonMapper personMapper,
                             StudyRoomMapper studyRoomMapper) {
        this.personMapper = personMapper;
        this.studyRoomMapper = studyRoomMapper;
    }

    public ReservationView convertReservationToReservationView(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        return new ReservationView(
                reservation.getId(),
                personMapper.convertPersonToPersonView(reservation.getStudent()),
                studyRoomMapper.convertStudyRoomToStudyRoomView(reservation.getRoom()),
                reservation.getDate(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getSeatsReserved(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getCancelledAt()
        );
    }
}

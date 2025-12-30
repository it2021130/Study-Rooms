package gr.hua.dit.studyrooms.core.service.mapper;

import gr.hua.dit.studyrooms.core.model.StudyRoom;
import gr.hua.dit.studyrooms.core.service.model.RoomAvailabilityView;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AvailabilityMapper {

    public RoomAvailabilityView toView(
            StudyRoom room,
            LocalDate date,
            int availableSeats
    ) {
        return new RoomAvailabilityView(
                room.getId(),
                room.getName(),
                room.getCapacity(),
                availableSeats,
                date.atTime(room.getOpeningTime()),
                date.atTime(room.getClosingTime())
        );
    }
}

package gr.hua.dit.studyrooms.core.service.model;
import java.time.LocalTime;

/**
 * StudyRoomView DTO – information exposed to clients.
 */
public record StudyRoomView(
        long id,
        String name,
        int capacity,
        LocalTime openingHour,
        LocalTime closingHour
) {}

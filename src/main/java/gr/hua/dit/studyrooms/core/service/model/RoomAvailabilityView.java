package gr.hua.dit.studyrooms.core.service.model;

import java.time.LocalDateTime;

public record RoomAvailabilityView(
        long roomId,
        String roomName,
        int capacity,
        int available,
        LocalDateTime start,
        LocalDateTime end
) {}

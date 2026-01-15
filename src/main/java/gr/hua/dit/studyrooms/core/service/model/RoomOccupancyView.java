package gr.hua.dit.studyrooms.core.service.model;
/**
 * View DTO representing occupancy statistics of a study room.
 */
public record RoomOccupancyView(
        long roomId,
        String roomName,
        int capacity,
        int booked,
        int available
) {}

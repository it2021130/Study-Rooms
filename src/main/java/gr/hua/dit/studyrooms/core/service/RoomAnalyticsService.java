package gr.hua.dit.studyrooms.core.service;

import gr.hua.dit.studyrooms.core.service.model.RoomOccupancyView;

import java.time.LocalDate;
import java.util.List;
/**
 * Service for providing occupancy and usage analytics for study rooms.
 */
public interface RoomAnalyticsService {
    List<RoomOccupancyView> getOccupancy(LocalDate date);
}

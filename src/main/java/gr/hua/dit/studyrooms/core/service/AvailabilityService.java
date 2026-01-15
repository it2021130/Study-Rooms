package gr.hua.dit.studyrooms.core.service;

import gr.hua.dit.studyrooms.core.service.model.RoomAvailabilityView;

import java.time.LocalDate;
import java.util.List;
/**
 * Service for calculating study room availability for a given date.
 */
public interface AvailabilityService {

    List<RoomAvailabilityView> getAvailabilityForDate(LocalDate date);

}

package gr.hua.dit.studyrooms.core.service.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
/**
 * DTO for requesting the update of an existing study room.
 */
public record UpdateStudyRoomRequest(
        @NotNull @Positive Long id,
        @NotNull  String name,
        @NotNull @Positive int capacity,
        @NotNull  String openingHour,
        @NotNull  String closingHour
) {}

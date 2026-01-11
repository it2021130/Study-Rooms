package gr.hua.dit.studyrooms.core.service.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for requesting the creation of a StudyRoom.
 */
public record CreateStudyRoomRequest(
        @NotNull  String name,
        @NotNull @Positive int capacity,
        @NotNull String openingHour,
        @NotNull String closingHour
) {}

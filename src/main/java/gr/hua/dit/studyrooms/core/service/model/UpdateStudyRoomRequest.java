package gr.hua.dit.studyrooms.core.service.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateStudyRoomRequest(
        @NotNull @Positive Long id,
        @NotNull  String name,
        @NotNull @Positive int capacity,
        @NotNull  String openingHour,
        @NotNull  String closingHour
) {}

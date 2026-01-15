package gr.hua.dit.studyrooms.core.service.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
/**
 * DTO for requesting the creation of a study room reservation.
 */
public record  CreateReservationRequest(

        @NotNull @Positive long roomId,
        @NotNull LocalDate date,
        @NotNull @Positive int seatsRequested
) {}

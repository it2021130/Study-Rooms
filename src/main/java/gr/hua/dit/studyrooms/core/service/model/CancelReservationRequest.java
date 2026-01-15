package gr.hua.dit.studyrooms.core.service.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
/**
 * DTO for requesting the cancellation of a reservation.
 */
public record CancelReservationRequest(
        @NotNull @Positive Long reservationId
) {}

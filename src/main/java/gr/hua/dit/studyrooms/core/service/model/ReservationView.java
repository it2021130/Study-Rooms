package gr.hua.dit.studyrooms.core.service.model;

import gr.hua.dit.studyrooms.core.model.ReservationStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationView(
        long id,
        PersonView student,
        StudyRoomView room,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        int seatsReserved,
        ReservationStatus status,
        Instant createdAt,
        Instant cancelledAt
) {}

package gr.hua.dit.studyrooms.core.service;

import gr.hua.dit.studyrooms.core.service.model.CancelReservationRequest;
import gr.hua.dit.studyrooms.core.service.model.CreateReservationRequest;
import gr.hua.dit.studyrooms.core.service.model.ReservationView;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    List<ReservationView> getReservationsOfCurrentUser();
    ReservationView bookReservation(CreateReservationRequest req);
    ReservationView cancelReservation(CancelReservationRequest req);
    ReservationView cancelReservationByStaff(CancelReservationRequest req);
    List<ReservationView> getReservationHistoryOfCurrentUser();
    List<ReservationView> getAllActiveReservationsForStaff();
}
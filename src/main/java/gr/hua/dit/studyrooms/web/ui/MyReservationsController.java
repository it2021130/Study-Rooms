package gr.hua.dit.studyrooms.web.ui;

import gr.hua.dit.studyrooms.core.security.CurrentUserProvider;
import gr.hua.dit.studyrooms.core.service.ReservationService;
import gr.hua.dit.studyrooms.core.service.model.CancelReservationRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasRole('STUDENT')")
@Controller
@RequestMapping("/rooms")
public class MyReservationsController {

    private final ReservationService reservationService;

    public MyReservationsController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @GetMapping("/reservations/my")
    public String myReservations(Model model) {

        var reservations = reservationService.getReservationsOfCurrentUser();

        model.addAttribute("reservations", reservations);

        return "reservations/my_reservations";
    }
    @PostMapping("/reservations/cancel")
    public String cancelReservation(@RequestParam long reservationId, Model model) {

        var view = reservationService.cancelReservation(new CancelReservationRequest(reservationId));

        model.addAttribute("reservation", view);

        return "reservations/reservation_cancel_success";
    }
    @GetMapping("/reservations/history")
    public String history(Model model) {

        var history = reservationService.getReservationHistoryOfCurrentUser();

        model.addAttribute("history", history);

        return "reservations/history";
    }
}


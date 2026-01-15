package gr.hua.dit.studyrooms.web.rest;

import gr.hua.dit.studyrooms.core.service.AvailabilityService;
import gr.hua.dit.studyrooms.core.service.model.RoomAvailabilityView;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomsAvailabilityRestController {

    private final AvailabilityService availabilityService;

    public RoomsAvailabilityRestController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/availability")
    public List<RoomAvailabilityView> getAvailability(@RequestParam String date)
    {
        LocalDate d = LocalDate.parse(date);
        if (d.isBefore(LocalDate.now())) {
            return List.of(); // επιστρέφει κενή λίστα -> δεν εμφανίζει τίποτα στο Calendar
        }
        return availabilityService.getAvailabilityForDate(d);
    }
}

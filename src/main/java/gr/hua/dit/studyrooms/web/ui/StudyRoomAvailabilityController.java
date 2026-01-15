package gr.hua.dit.studyrooms.web.ui;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * UI controller for displaying the study room availability calendar to students.
 */
@PreAuthorize("hasRole('STUDENT')")
@Controller
public class StudyRoomAvailabilityController {
    @GetMapping("/room/calendar")
    public String showAvailabilityCalendar(Model model) {
        return "room/calendar";
    }
}
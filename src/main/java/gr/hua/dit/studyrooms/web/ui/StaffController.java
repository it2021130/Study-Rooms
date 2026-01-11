package gr.hua.dit.studyrooms.web.ui;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaffController {

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/dashboard")
    public String showDashboard(Model model) {
        return "staff/dashboard";
    }
}

package gr.hua.dit.studyrooms.web.ui;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomepageController {

    @GetMapping("/")
    public String showHomepage(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "homepage";
        }

        boolean isStaff = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"));

        if (isStaff) {
            return "redirect:/staff/dashboard";   // STAFF → Dashboard
        }
        return "redirect:/profile";                // STUDENT → Profile
    }
}
package gr.hua.dit.studyrooms.web.ui;

import gr.hua.dit.studyrooms.core.security.CurrentUser;
import gr.hua.dit.studyrooms.core.security.CurrentUserProvider;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * UI controller for displaying the authenticated student's profile information.
 */
@PreAuthorize("hasRole('STUDENT')")
@Controller
public class ProfileController {

    private final CurrentUserProvider currentUserProvider;

    public ProfileController(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {

        CurrentUser currentUser = currentUserProvider.getCurrentUser().orElse(null);

        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("me", currentUser);

        return "profile";
    }
}

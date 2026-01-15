package gr.hua.dit.studyrooms.web.ui;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.GrantedAuthority;


/**
 * UI controller for user authentication (login and logout).
 */
@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(
            final Authentication authentication,
            final HttpServletRequest request,
            final Model model
    ) {
        if (AuthUtils.isAuthenticated(authentication)) {

            // --- Redirect based on ROLE ---
            for (GrantedAuthority auth : authentication.getAuthorities()) {

                if (auth.getAuthority().equals("ROLE_STAFF")) {
                    return "redirect:/staff/dashboard"; // STAFF -> dashboard
                }

                if (auth.getAuthority().equals("ROLE_STUDENT")) {
                    return "redirect:/profile"; // student -> profile
                }
            }
            // fallback
            return "redirect:/profile";
        }

        // Messages
        if (request.getParameter("error") != null) {
            model.addAttribute("error", "Invalid email or password.");
        }
        if (request.getParameter("logout") != null) {
            model.addAttribute("message", "You have been logged out.");
        }

        return "login";
    }

    @GetMapping("/logout")
    public String logout(final Authentication authentication) {
        if (AuthUtils.isAnonymous(authentication)) {
            return "redirect:/login";
        }
        return "logout";
    }
}
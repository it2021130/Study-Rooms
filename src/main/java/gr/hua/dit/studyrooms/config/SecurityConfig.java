package gr.hua.dit.studyrooms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration.
 */
@Configuration
@EnableMethodSecurity // enables @PreAuthorize
public class SecurityConfig {

    // TODO API Security (stateless - JWT based)

    /**
     * UI chain {@code "/**"} (stateful - cookie based).
     */
    @Bean
    @Order(2)
    public SecurityFilterChain uiChain(final HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register").permitAll()
                        // Student-only pages
                        .requestMatchers("/profile","/room/calendar", "/rooms/reserve", "/rooms/reservations/my","/rooms/reservations/cancel", "/rooms/reservations/history").hasRole("STUDENT")
                        // Staff-only pages
                        .requestMatchers("/staff/**").hasRole("STAFF")
                        .requestMatchers("/staff/dashboard",
                                "/room/create",
                                "/room/edit/**",
                                "/room/list",
                                "/bookings/manage","/bookings/cancel",
                                "/stats/**"
                        ).hasRole("STAFF")
                        .requestMatchers( "/logout").authenticated()
                        .requestMatchers("/api/**").authenticated()
                        //Everything else is denied by default
                        .anyRequest().denyAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {

                            boolean isStaff = authentication.getAuthorities()
                                    .stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"));

                            if (isStaff) {
                                response.sendRedirect("/staff/dashboard");
                            } else {
                                response.sendRedirect("/profile");
                            }
                        })
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
package gr.hua.dit.studyrooms.core.service.model;

import gr.hua.dit.studyrooms.core.model.PersonType;
import jakarta.validation.constraints.*;

/**
 * DTO for requesting the creation (registration) of a Person.
 */
public record CreatePersonRequest(
        @NotNull PersonType type,
        @NotNull @NotBlank @Size(max = 20) String studentId,
        @NotNull @NotBlank @Size(max = 100) String firstName,
        @NotNull @NotBlank @Size(max = 100)  String lastName,
        @NotNull @NotBlank @Size(max = 100) @Email String emailAddress,
        @NotNull @NotBlank @Size(max = 18)  String mobilePhoneNumber,
        @NotNull @NotBlank @Size(max = 4)  String rawPassword
) {}
package dev.sorokin.eventmanager.users;


import jakarta.validation.constraints.*;

public record SingUpRequest(
        @NotBlank
        @Size(min = 5)
        String login,
        @NotBlank
        @Size(min = 5)
        String password,
        @Min(18)
        Integer age
) {
}

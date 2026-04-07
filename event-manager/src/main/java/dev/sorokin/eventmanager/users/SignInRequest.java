package dev.sorokin.eventmanager.users;

import jakarta.validation.constraints.*;

public record SignInRequest(
        @NotBlank
        String login,
        @NotBlank
        String password,
        @NotNull
        Integer age
) {
}

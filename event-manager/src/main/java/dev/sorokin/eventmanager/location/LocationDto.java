package dev.sorokin.eventmanager.location;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public record LocationDto(
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        Integer id,
        String name,
        String address,
        @Min(5)
        Integer capacity,
        String description

) {
}

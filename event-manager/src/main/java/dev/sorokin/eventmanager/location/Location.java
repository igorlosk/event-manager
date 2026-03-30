package dev.sorokin.eventmanager.location;

public record Location(
        Integer id,
        String name,
        String address,
        Integer capacity,
        String description
) {
}

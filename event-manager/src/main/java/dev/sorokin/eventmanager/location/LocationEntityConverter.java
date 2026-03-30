package dev.sorokin.eventmanager.location;

import org.springframework.stereotype.Component;

@Component
public class LocationEntityConverter {

    public LocationEntity toEntity(Location location) {
        return new LocationEntity(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );

    }

    public Location toDomain(LocationEntity locationEntity) {
        return new Location(
                locationEntity.id,
                locationEntity.name,
                locationEntity.address,
                locationEntity.capacity,
                locationEntity.description
        );
    }

}

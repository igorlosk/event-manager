package dev.sorokin.eventmanager.location;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationEntityConverter locationEntityConverter;

    public LocationService(LocationRepository locationRepository, LocationEntityConverter locationEntityConverter) {
        this.locationRepository = locationRepository;
        this.locationEntityConverter = locationEntityConverter;
    }

    public Location createLocation(Location location) {
        LocationEntity entityLocation = locationEntityConverter.toEntity(location);

        return locationEntityConverter.toDomain(locationRepository.save(entityLocation));
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(locationEntityConverter::toDomain)
                .toList();

    }

    public void deleteLocation(Integer id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Location does not exists by id=%s".formatted(id));
        }
        locationRepository.deleteById(id);
    }

    public Location getLocationById(Integer id) {
        LocationEntity location = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location does not exists by id=%s".formatted(id)));
        return locationEntityConverter.toDomain(location);
    }

    public Location updateLocation(Location location, Integer id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Location does not exists by id=%s".formatted(id));
        }

        locationRepository.updateLocation(
                id,
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );

        return locationEntityConverter.toDomain(locationRepository.findById(id).orElseThrow());
    }
}

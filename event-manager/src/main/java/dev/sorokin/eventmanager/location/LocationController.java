package dev.sorokin.eventmanager.location;

import jakarta.validation.Valid;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/locations")
public class LocationController {

    private final static Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    private final LocationDtoConverter locationDtoConverter;

    public LocationController(LocationService locationService, LocationDtoConverter locationDtoConverter) {
        this.locationService = locationService;
        this.locationDtoConverter = locationDtoConverter;
    }

    @GetMapping
    public List<LocationDto> getAllLocations() {
        LOGGER.info("Get request to get all locations");
        return locationService.getAllLocations()
                .stream()
                .map(locationDtoConverter::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable("id") Integer id) {
        LOGGER.info("Get request to get location by id {}", id);
        Location locationDto = locationService.getLocationById(id);
        return ResponseEntity.ok(locationDtoConverter.toDto(locationDto));
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestBody @Valid LocationDto locationDto) {
        LOGGER.info("Get request to create location: location={}", locationDto);

        Location createdLocation = locationService.createLocation(locationDtoConverter.toDomain(locationDto));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(locationDtoConverter.toDto(createdLocation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("id") Integer id) {
        LOGGER.info("Get request to delete location: id={}", id);
        locationService.deleteLocation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable("id") Integer id,
            @RequestBody @Valid LocationDto locationDto) {
        LOGGER.info("Get request to update location: id={}", id);
        Location location = locationService.updateLocation(locationDtoConverter.toDomain(locationDto), id);
        return ResponseEntity.ok(locationDtoConverter.toDto(location));
    }


}

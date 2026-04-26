package dev.sorokin.eventmanager.event;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestDtoToEventDtoMapper {

    public EventCreateRequestDto toRequestDto(EventDto eventDto) {
        return new EventCreateRequestDto(
                eventDto.name(),
                eventDto.maxPlaces(),
                eventDto.date(),
                eventDto.cost(),
                eventDto.duration(),
                eventDto.locationId()
        );
    }

    public EventDto toEventDto(EventCreateRequestDto eventCreateRequestDto) {
        return new EventDto(
                null,
                eventCreateRequestDto.name(),
                null,
                eventCreateRequestDto.maxPlaces(),
                null,
                eventCreateRequestDto.date(),
                eventCreateRequestDto.cost(),
                eventCreateRequestDto.duration(),
                eventCreateRequestDto.locationId(),
                null,
                List.of()
                );
    }

}

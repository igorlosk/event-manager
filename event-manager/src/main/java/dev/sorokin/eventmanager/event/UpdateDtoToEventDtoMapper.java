package dev.sorokin.eventmanager.event;

import org.springframework.stereotype.Component;

@Component
public class UpdateDtoToEventDtoMapper {

    public EventUpdateRequestDto toUpdateDto(EventDto eventDto) {
        return new EventUpdateRequestDto(
                eventDto.name(),
                eventDto.maxPlaces(),
                eventDto.date(),
                eventDto.cost(),
                eventDto.duration(),
                eventDto.locationId()
        );
    }

    public EventDto toEventDto(EventUpdateRequestDto eventUpdateRequestDto) {
        return new EventDto(
                null,
                eventUpdateRequestDto.name(),
                null,
                eventUpdateRequestDto.maxPlaces(),
                null,
                eventUpdateRequestDto.date(),
                eventUpdateRequestDto.cost(),
                eventUpdateRequestDto.duration(),
                eventUpdateRequestDto.locationId(),
                null
        );
    }
}

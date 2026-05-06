package dev.sorokin.eventmanager.event;

import org.springframework.stereotype.Component;

@Component
public class RequestDtoToEventDtoMapper {

    private final DateTimeConverter dateTimeConverter;

    public RequestDtoToEventDtoMapper(DateTimeConverter dateTimeConverter) {
        this.dateTimeConverter = dateTimeConverter;
    }

//    public EventCreateRequestDto toRequestDto(EventDto eventDto) {
//        return new EventCreateRequestDto(
//                eventDto.name(),
//                eventDto.maxPlaces(),
//                eventDto.date(),
//                eventDto.cost(),
//                eventDto.duration(),
//                eventDto.locationId()
//        );
//    }

    public EventDto toEventDto(EventCreateRequestDto eventCreateRequestDto) {
        return new EventDto(
                null,
                eventCreateRequestDto.name(),
                null,
                eventCreateRequestDto.maxPlaces(),
                null,
                dateTimeConverter.formatToStringFromOffsetDateTime(eventCreateRequestDto.date()),
                eventCreateRequestDto.cost(),
                eventCreateRequestDto.duration(),
                eventCreateRequestDto.locationId(),
                null
        );
    }
}

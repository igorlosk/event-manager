package dev.sorokin.eventmanager.events;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RequestDtoToEventDtoConverter {


    public EventDto toEventDto(EventCreateRequestDto eventCreateRequestDto) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        OffsetDateTime odt = OffsetDateTime.parse(eventCreateRequestDto.date(), formatter);

        LocalDateTime ldt = odt.toLocalDateTime();

        return new EventDto(
                null,
                eventCreateRequestDto.name(),
                null,
                eventCreateRequestDto.maxPlaces(),
                null,
                ldt,
                eventCreateRequestDto.cost(),
                eventCreateRequestDto.duration(),
                eventCreateRequestDto.locationId(),
                null,
                null
        );
    }

    public EventCreateRequestDto toEventCreateRequestDto(EventDto eventDto) {

        OffsetDateTime offsetDateTime = OffsetDateTime.of(eventDto.date(), ZoneOffset.UTC);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        String convertedDate = offsetDateTime.format(dateTimeFormatter);

        return new EventCreateRequestDto(
                eventDto.name(),
                eventDto.maxPlaces(),
                convertedDate,
                eventDto.cost(),
                eventDto.duration(),
                eventDto.locationId()
        );
    }
}

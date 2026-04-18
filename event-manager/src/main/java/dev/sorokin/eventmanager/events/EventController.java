package dev.sorokin.eventmanager.events;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventToDtoConverter eventToDtoConverter;
    private final EventService eventService;

    public EventController(EventToDtoConverter eventToDtoConverter, EventService eventService) {
        this.eventToDtoConverter = eventToDtoConverter;
        this.eventService = eventService;
    }


//    @PostMapping
//    public ResponseEntity<EventCreateRequestDto> eventCreate (
//            @RequestBody EventCreateRequestDto eventCreateRequestDto
//    ){
//        Event createdEvent = eventService.createEvent(eventDtoConverter.toDomain(eventCreateRequestDto));
//
//    }
}

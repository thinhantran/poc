package com.conference.joinusapi.controller;

import com.conference.joinusapi.dto.EventResponse;
import com.conference.joinusapi.model.Event;
import com.conference.joinusapi.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;

    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(event -> EventResponse.builder()
                        .id(event.getId())
                        .name(event.getName())
                        .type(event.getType())
                        .location(event.getLocation())
                        .dateTime(event.getDateTime())
                        .description(event.getDescription())
                        .price(event.getPrice())
                        .imageUrl(event.getImageUrl())
                        .build()
                )
                .toList();
    }

    @GetMapping("/search")
    public List<EventResponse> searchEventsByName(@RequestParam("name") String name) {
        List<Event> events = eventRepository.findByNameContainingIgnoreCase(name);
        return events.stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    private EventResponse mapToEventResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .type(event.getType())
                .location(event.getLocation())
                .dateTime(event.getDateTime())
                .description(event.getDescription())
                .price(event.getPrice())
                .imageUrl(event.getImageUrl())
                .build();
    }
}

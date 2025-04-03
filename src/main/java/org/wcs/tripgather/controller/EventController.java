package org.wcs.tripgather.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.wcs.tripgather.dto.EventDTO;
import org.wcs.tripgather.service.EventService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    //CRUD
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        EventDTO event = eventService.getEventById(id);
        return event != null ? new ResponseEntity<>(event, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        EventDTO createdEvent = eventService.createEvent(eventDTO);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO) {
        EventDTO updatedEvent = eventService.updateEvent(id, eventDTO);
        if (updatedEvent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventService.deleteEvent(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventDTO>> getFilteredEvents(@RequestParam(required = false) String localisation,
                                                            @RequestParam(required = false) String gender,
                                                            @RequestParam(required = false) String title,
                                                            @RequestParam(required = false) String fromDate,
                                                            @RequestParam(required = false) String toDate) {
        System.out.println("Filtrage avec : localisation=" + localisation + ", gender=" + gender + ", fromDate=" + fromDate + ", toDate=" + toDate);
        List<EventDTO> filteredEvents = eventService.getFilteredEvents(localisation, gender,title, fromDate, toDate);
        if (filteredEvents.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(filteredEvents, HttpStatus.OK);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<EventDTO> addMemberToEvent(
            @PathVariable Long eventId,
            @RequestBody Map<String, Long> request
    ) {
        Long userId = request.get("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        EventDTO eventDTO = eventService.addMemberToEvent(eventId, userId);
        return ResponseEntity.ok(eventDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventDTO>> getEventsByUser(@PathVariable Long userId) {
        List<EventDTO> events = eventService.getEventsByUserId(userId);
        return ResponseEntity.ok(events);
    }
}

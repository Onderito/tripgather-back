package org.wcs.tripgather.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.wcs.tripgather.dto.EventDTO;
import org.wcs.tripgather.service.EventService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEvents() {
        List<EventDTO> events = List.of(new EventDTO(), new EventDTO());
        when(eventService.getAllEvents()).thenReturn(events);

        ResponseEntity<List<EventDTO>> response = eventController.getAllEvents();

        assertEquals(OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetEventById_WhenEventExists() {
        EventDTO event = new EventDTO();
        when(eventService.getEventById(1L)).thenReturn(event);

        ResponseEntity<EventDTO> response = eventController.getEventById(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    @Test
    void testGetEventById_WhenEventDoesNotExist() {
        when(eventService.getEventById(1L)).thenReturn(null);

        ResponseEntity<EventDTO> response = eventController.getEventById(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreateEvent() {
        EventDTO eventDTO = new EventDTO();
        EventDTO createdEvent = new EventDTO();
        when(eventService.createEvent(eventDTO)).thenReturn(createdEvent);

        ResponseEntity<EventDTO> response = eventController.createEvent(eventDTO);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(createdEvent, response.getBody());
    }

    @Test
    void testUpdateEvent_WhenEventExists() {
        EventDTO eventDTO = new EventDTO();
        EventDTO updatedEvent = new EventDTO();
        when(eventService.updateEvent(1L, eventDTO)).thenReturn(updatedEvent);

        ResponseEntity<EventDTO> response = eventController.updateEvent(1L, eventDTO);

        assertEquals(OK, response.getStatusCode());
        assertEquals(updatedEvent, response.getBody());
    }

    @Test
    void testUpdateEvent_WhenEventDoesNotExist() {
        EventDTO eventDTO = new EventDTO();
        when(eventService.updateEvent(1L, eventDTO)).thenReturn(null);

        ResponseEntity<EventDTO> response = eventController.updateEvent(1L, eventDTO);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteEvent_WhenEventExists() {
        when(eventService.deleteEvent(1L)).thenReturn(true);

        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteEvent_WhenEventDoesNotExist() {
        when(eventService.deleteEvent(1L)).thenReturn(false);

        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
    }
}
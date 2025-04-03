package org.wcs.tripgather.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wcs.tripgather.dto.EventDTO;
import org.wcs.tripgather.mapper.EventMapper;
import org.wcs.tripgather.model.Event;
import org.wcs.tripgather.repository.EventRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    @Test
    void testGetAllEvents_WhenNoEventsExist() {
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());
        List<EventDTO> events = eventService.getAllEvents();
        assertTrue(events.isEmpty());
    }

    @Test
    void testGetEventById_WhenEventExists() {
        Event event = new Event();
        event.setId(1L);
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(1L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventMapper.convertToDTO(event)).thenReturn(eventDTO);

        EventDTO foundEvent = eventService.getEventById(1L);
        assertNotNull(foundEvent);
        assertEquals(1L, foundEvent.getId());
    }

    @Test
    void testGetEventById_WhenEventDoesNotExist() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> eventService.getEventById(1L));
    }

    @Test
    void testCreateEvent() {
        EventDTO eventDTO = new EventDTO();
        Event event = new Event();
        Event savedEvent = new Event();
        EventDTO savedEventDTO = new EventDTO();

        when(eventMapper.convertToEntity(eventDTO)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(savedEvent);
        when(eventMapper.convertToDTO(savedEvent)).thenReturn(savedEventDTO);

        EventDTO result = eventService.createEvent(eventDTO);
        assertNotNull(result);
    }
}
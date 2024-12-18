package sports.demoapi.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sports.demoapi.model.EventStatus;
import sports.demoapi.model.SportsEvent;
import sports.demoapi.repository.SportsEventRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SportsEventServiceImplTest {

    @Mock
    private SportsEventRepository repository;

    @InjectMocks
    private SportsEventServiceImpl target;

    /**
     * {@link SportsEventServiceImpl#save(SportsEvent)}
     */
    @Test
    void shouldSaveEventToRepository() {

        SportsEvent event = new SportsEvent();

        when(repository.save(event)).thenReturn(event);

        SportsEvent result = target.save(event);

        verify(repository).save(event);
        assertSame(event, result);
    }

    /**
     * {@link SportsEventServiceImpl#getById(long)}
     */
    @Test
    void shouldGetEventById() {

        SportsEvent event = new SportsEvent();
        event.setId(3L);

        Optional<SportsEvent> result = target.getById(1L);
        assertFalse(result.isPresent());

        doReturn(Optional.of(event)).when(repository).findById(event.getId());
        result = target.getById(3L);
        assertTrue(result.isPresent());
        assertSame(event, result.get());

        verify(repository, times(2)).findById(anyLong());
    }

    /**
     * {@link SportsEventServiceImpl#getBy(String, EventStatus)}
     */
    @Test
    void shouldGetEventsByType() {

        String type = "Type";

        SportsEvent event = new SportsEvent();
        SportsEvent event2 = new SportsEvent();

        List<SportsEvent> listOfEvents = List.of(event, event2);

        when(repository.findByType(type)).thenReturn(listOfEvents);

        List<SportsEvent> result = target.getBy(type, null);

        assertSame(listOfEvents, result);
        verify(repository).findByType(type);
    }

    /**
     * {@link SportsEventServiceImpl#getBy(String, EventStatus)}
     */
    @Test
    void shouldGetEventsByStatus() {

        EventStatus status = EventStatus.INACTIVE;

        SportsEvent event = new SportsEvent();
        SportsEvent event2 = new SportsEvent();

        List<SportsEvent> listOfEvents = List.of(event, event2);

        when(repository.findByStatus(status)).thenReturn(listOfEvents);

        List<SportsEvent> result = target.getBy(null, status);

        assertSame(listOfEvents, result);
        verify(repository).findByStatus(status);
    }

    /**
     * {@link SportsEventServiceImpl#getBy(String, EventStatus)}
     */
    @Test
    void shouldGetEventsByTypeAndStatus() {

        String type = "Type";
        EventStatus status = EventStatus.INACTIVE;

        SportsEvent event = new SportsEvent();
        SportsEvent event2 = new SportsEvent();

        List<SportsEvent> listOfEvents = List.of(event, event2);

        when(repository.findByTypeAndStatus(type, status)).thenReturn(listOfEvents);

        List<SportsEvent> result = target.getBy(type, status);

        assertSame(listOfEvents, result);
        verify(repository).findByTypeAndStatus(type, status);
    }

    /**
     * {@link SportsEventServiceImpl#getBy(String, EventStatus)}
     */
    @Test
    void shouldGetAllEvents() {

        SportsEvent event = new SportsEvent();
        SportsEvent event2 = new SportsEvent();

        List<SportsEvent> listOfEvents = List.of(event, event2);

        when(repository.findAll()).thenReturn(listOfEvents);

        List<SportsEvent> result = target.getBy(null, null);

        assertSame(listOfEvents, result);
        verify(repository).findAll();
    }
}

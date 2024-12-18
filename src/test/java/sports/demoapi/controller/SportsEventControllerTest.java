package sports.demoapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;
import sports.demoapi.controller.dto.SportsEventInputDto;
import sports.demoapi.controller.dto.SportsEventResponseDto;
import sports.demoapi.controller.exception.ResourceNotFoundException;
import sports.demoapi.controller.mapper.impl.SportsEventMapper;
import sports.demoapi.controller.validator.SportsEventStatusChangeValidator;
import sports.demoapi.model.EventStatus;
import sports.demoapi.model.SportsEvent;
import sports.demoapi.service.SportsEventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SportsEventControllerTest {

    @InjectMocks
    private SportsEventController target;

    @Mock
    private SportsEventService service;
    @Mock
    private SportsEventMapper mapper;
    @Mock
    private SportsEventStatusChangeValidator statusValidator;

    /**
    * {@link SportsEventController#addSportsEvent(SportsEventInputDto)}
    */
    @Test
    void shouldAddSportsEvent() {

        SportsEvent sportsEvent = new SportsEvent();
        SportsEventInputDto sportsEventInputDto = new SportsEventInputDto();
        SportsEventResponseDto sportsEventResponseDto = new SportsEventResponseDto();

        when(mapper.convertToEntity(sportsEventInputDto)).thenReturn(sportsEvent);
        when(mapper.convertToResponseDto(sportsEvent)).thenReturn(sportsEventResponseDto);
        when(service.save(sportsEvent)).thenReturn(sportsEvent);

        SportsEventResponseDto result = target.addSportsEvent(sportsEventInputDto);

        assertSame(sportsEventResponseDto, result);
        verify(mapper).convertToResponseDto(sportsEvent);
        verify(mapper).convertToEntity(sportsEventInputDto);
        verify(service).save(sportsEvent);
    }

    /**
     * {@link SportsEventController#addSportsEvent(SportsEventInputDto)}
     */
    @Test
    void shouldReturnBadRequestStatusInCaseOfDataIntegrityViolationException() {

        SportsEvent sportsEvent = new SportsEvent();
        SportsEventInputDto sportsEventInputDto = new SportsEventInputDto();

        when(mapper.convertToEntity(sportsEventInputDto)).thenReturn(sportsEvent);
        when(service.save(sportsEvent)).thenThrow(DataIntegrityViolationException.class);

        SportsEventResponseDto result = null;
        DataIntegrityViolationException exception = null;
        try {
            result = target.addSportsEvent(sportsEventInputDto);
        } catch (DataIntegrityViolationException e) {
            exception = e;
        }

        assertNull(result);
        assertNotNull(exception);
        verify(mapper).convertToEntity(sportsEventInputDto);
        verify(service).save(sportsEvent);
        verify(mapper, times(0)).convertToResponseDto(sportsEvent);
    }

    /**
     * {@link SportsEventController#getSportsEvent(long)}
     */
    @Test
    void shouldGetEventById() {

        SportsEvent sportsEvent = new SportsEvent();
        SportsEventResponseDto responseDto = new SportsEventResponseDto();

        when(mapper.convertToResponseDto(sportsEvent)).thenReturn(responseDto);
        when(service.getById(1L)).thenReturn(Optional.of(sportsEvent));

        SportsEventResponseDto result = target.getSportsEvent(1L);

        assertSame(responseDto, result);
        verify(service).getById(1L);
        verify(mapper).convertToResponseDto(sportsEvent);
    }

    /**
     * {@link SportsEventController#getSportsEvent(long)}
     */
    @Test
    void shouldThrowExceptionIfSportsEventNotFoundDuringEventGetting() {

        when(service.getById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> target.getSportsEvent(1L));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("No event exists with id: 1"));
        verify(service).getById(1L);
        verifyNoInteractions(mapper);
    }

    /**
     * {@link SportsEventController#updateSportsEventStatus(long, EventStatus)}
     */
    @Test
    void shouldThrowExceptionIfSportsEventNotFoundForDuringStatusUpdate() {

        when(service.getById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> target.updateSportsEventStatus(1L, EventStatus.ACTIVE));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("No event exists with id: 1"));
        verify(service, times(0)).save(any());
        verifyNoInteractions(mapper);
    }

    /**
     * {@link SportsEventController#updateSportsEventStatus(long, EventStatus)}
     */
    @Test
    void shouldUpdateSportsEventStatus() {

        SportsEvent sportsEvent = new SportsEvent();
        sportsEvent.setStatus(EventStatus.INACTIVE);
        sportsEvent.setStartTime(LocalDateTime.of(1990, 1, 1, 10, 0));

        EventStatus newStatus = EventStatus.ACTIVE;

        SportsEventResponseDto responseDto = new SportsEventResponseDto();

        when(service.getById(1L)).thenReturn(Optional.of(sportsEvent));
        when(statusValidator.isValidChange(sportsEvent.getStatus(), newStatus, sportsEvent.getStartTime()))
                .thenReturn(true);
        when(mapper.convertToResponseDto(any())).thenReturn(responseDto);

        ArgumentCaptor<SportsEvent> argumentCaptor = ArgumentCaptor.forClass(SportsEvent.class);

        SportsEventResponseDto result = target.updateSportsEventStatus(1L, newStatus);

        verify(service).save(argumentCaptor.capture());

        assertNotNull(result);
        assertEquals(newStatus, argumentCaptor.getValue().getStatus());
        assertEquals(newStatus, sportsEvent.getStatus());
        verify(service).save(sportsEvent);
        verify(mapper).convertToResponseDto(any());
    }

    /**
     * {@link SportsEventController#updateSportsEventStatus(long, EventStatus)}
     */
    @Test
    void shouldNotUpdateSportsEventStatusIfValidationNotPassed() {

        SportsEvent sportsEvent = new SportsEvent();
        sportsEvent.setStatus(EventStatus.INACTIVE);
        sportsEvent.setStartTime(LocalDateTime.of(1990, 1, 1, 10, 0));

        EventStatus newStatus = EventStatus.ACTIVE;

        when(service.getById(1L)).thenReturn(Optional.of(sportsEvent));
        when(statusValidator.isValidChange(sportsEvent.getStatus(), newStatus, sportsEvent.getStartTime()))
                .thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> target.updateSportsEventStatus(1L, newStatus));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("202 ACCEPTED"));
        verify(service, times(0)).save(sportsEvent);
        verifyNoInteractions(mapper);
    }

    /**
     * {@link SportsEventController#getSportsEvents(SportsEventInputDto)}
     */
    @Test
    void shouldGetSportEvents() {

        SportsEventInputDto inputDto = new SportsEventInputDto();
        inputDto.setType("Type");
        inputDto.setStatus(EventStatus.ACTIVE);

        SportsEvent event = new SportsEvent();
        SportsEvent event2 = new SportsEvent();
        List<SportsEvent> listOfEvents = List.of(event, event2);

        when(service.getBy(inputDto.getType(), inputDto.getStatus())).thenReturn(listOfEvents);
        when(mapper.convertToResponseDto(any())).thenReturn(new SportsEventResponseDto());

        List<SportsEventResponseDto> result = target.getSportsEvents(inputDto);

        assertEquals(2, result.size());
        verify(service).getBy(inputDto.getType(), inputDto.getStatus());
        verify(mapper, times(2)).convertToResponseDto(any());
    }
}

package sports.demoapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import sports.demoapi.controller.dto.SportsEventInputDto;
import sports.demoapi.controller.dto.SportsEventResponseDto;
import sports.demoapi.controller.exception.ResourceNotFoundException;
import sports.demoapi.controller.mapper.impl.SportsEventMapper;
import sports.demoapi.controller.validator.SportsEventStatusChangeValidator;
import sports.demoapi.model.EventStatus;
import sports.demoapi.model.SportsEvent;
import sports.demoapi.service.SportsEventService;

import java.time.LocalDateTime;
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
    void shouldReturnNotFoundStatusIfEventNotFound() {

        SportsEvent sportsEvent = new SportsEvent();

        when(service.getById(1L)).thenReturn(Optional.empty());

        SportsEventResponseDto result = target.getSportsEvent(1L);

        assertNull(result);
        verify(service).getById(1L);
        verify(mapper, times(0)).convertToResponseDto(sportsEvent);
    }

    /**
     * {@link SportsEventController#updateSportsEvent(long, SportsEventInputDto)}
     */
    @Test
    void shouldThrowExceptionIfSportsEventNotFound() {

        SportsEventInputDto inputDto = new SportsEventInputDto();

        when(service.getById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = null;

        try {
            target.updateSportsEvent(1L, inputDto);
        } catch (ResourceNotFoundException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals("No event exists with id: 1", exception.getMessage());

        verify(service, times(0)).save(any());
        verifyNoInteractions(mapper);
    }

    /**
     * {@link SportsEventController#updateSportsEvent(long, SportsEventInputDto)}
     */
    @Test
    void shouldUpdateSportsEventType() {

        SportsEvent sportsEvent = new SportsEvent();
        sportsEvent.setType("Football");

        SportsEventInputDto inputDto = new SportsEventInputDto();
        inputDto.setType("Basketball");

        SportsEventResponseDto responseDto = new SportsEventResponseDto();

        when(service.getById(1L)).thenReturn(Optional.of(sportsEvent));
        when(mapper.convertToResponseDto(any())).thenReturn(responseDto);

        ArgumentCaptor<SportsEvent> argumentCaptor = ArgumentCaptor.forClass(SportsEvent.class);

        SportsEventResponseDto result = target.updateSportsEvent(1L, inputDto);

        verify(service).save(argumentCaptor.capture());

        assertNotNull(result);
        assertEquals(inputDto.getType(), argumentCaptor.getValue().getType());
        assertEquals(inputDto.getType(), sportsEvent.getType());
        verify(service).save(sportsEvent);
        verify(mapper).convertToResponseDto(any());
    }

    /**
     * {@link SportsEventController#updateSportsEvent(long, SportsEventInputDto)}
     */
    @Test
    void shouldUpdateSportsEventStatus() {

        SportsEvent sportsEvent = new SportsEvent();
        sportsEvent.setStatus(EventStatus.INACTIVE);
        sportsEvent.setStartTime(LocalDateTime.of(1990, 1, 1, 10, 0));

        SportsEventInputDto inputDto = new SportsEventInputDto();
        inputDto.setStatus(EventStatus.ACTIVE);

        SportsEventResponseDto responseDto = new SportsEventResponseDto();

        when(service.getById(1L)).thenReturn(Optional.of(sportsEvent));
        when(statusValidator.isValidChange(sportsEvent.getStatus(), inputDto.getStatus(), sportsEvent.getStartTime()))
                .thenReturn(true);
        when(mapper.convertToResponseDto(any())).thenReturn(responseDto);

        ArgumentCaptor<SportsEvent> argumentCaptor = ArgumentCaptor.forClass(SportsEvent.class);

        SportsEventResponseDto result = target.updateSportsEvent(1L, inputDto);

        verify(service).save(argumentCaptor.capture());

        assertNotNull(result);
        assertEquals(inputDto.getStatus(), argumentCaptor.getValue().getStatus());
        assertEquals(inputDto.getStatus(), sportsEvent.getStatus());
        verify(service).save(sportsEvent);
        verify(mapper).convertToResponseDto(any());
    }

    /**
     * {@link SportsEventController#updateSportsEvent(long, SportsEventInputDto)}
     */
    @Test
    void shouldNotUpdateSportsEventStatusIfValidationNotPassed() {

        SportsEvent sportsEvent = new SportsEvent();
        sportsEvent.setStatus(EventStatus.INACTIVE);
        sportsEvent.setStartTime(LocalDateTime.of(1990, 1, 1, 10, 0));

        SportsEventInputDto inputDto = new SportsEventInputDto();
        inputDto.setStatus(EventStatus.ACTIVE);

        SportsEventResponseDto responseDto = new SportsEventResponseDto();

        when(service.getById(1L)).thenReturn(Optional.of(sportsEvent));
        when(statusValidator.isValidChange(sportsEvent.getStatus(), inputDto.getStatus(), sportsEvent.getStartTime()))
                .thenReturn(false);
        when(mapper.convertToResponseDto(any())).thenReturn(responseDto);

        ArgumentCaptor<SportsEvent> argumentCaptor = ArgumentCaptor.forClass(SportsEvent.class);

        SportsEventResponseDto result = target.updateSportsEvent(1L, inputDto);

        verify(service).save(argumentCaptor.capture());

        assertNotNull(result);
        assertNotEquals(inputDto.getStatus(), argumentCaptor.getValue().getStatus());
        assertNotEquals(inputDto.getStatus(), sportsEvent.getStatus());
        verify(service).save(sportsEvent);
        verify(mapper).convertToResponseDto(any());
    }
}

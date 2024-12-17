package sports.demoapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import sports.demoapi.controller.dto.SportsEventInputDto;
import sports.demoapi.controller.dto.SportsEventResponseDto;
import sports.demoapi.controller.mapper.impl.SportsEventMapper;
import sports.demoapi.model.SportsEvent;
import sports.demoapi.service.SportsEventService;

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
}

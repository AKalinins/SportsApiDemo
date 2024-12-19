package sports.demoapi.controller.mapper.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import sports.demoapi.controller.dto.SportsEventInputDto;
import sports.demoapi.controller.dto.SportsEventResponseDto;
import sports.demoapi.model.SportsEvent;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SportsEventMapperTest {

    @InjectMocks
    private SportsEventMapper target;

    @Mock
    private ModelMapper modelMapper;

    /**
     * {@link SportsEventMapper#convertToEntity(SportsEventInputDto)}
     */
    @Test
    void shouldConvertInputToEntity() {

        SportsEventInputDto inputDto = new SportsEventInputDto();
        SportsEvent sportsEvent = new SportsEvent();

        when(modelMapper.map(inputDto, SportsEvent.class)).thenReturn(sportsEvent);

        SportsEvent result = target.convertToEntity(inputDto);

        assertSame(sportsEvent, result);
        verify(modelMapper).map(inputDto, SportsEvent.class);
    }

    /**
     * {@link SportsEventMapper#convertToResponseDto(SportsEvent)}
     */
    @Test
    void shouldConvertEntityToResponse() {

        SportsEvent sportsEvent = new SportsEvent();
        SportsEventResponseDto responseDto = new SportsEventResponseDto();

        when(modelMapper.map(sportsEvent, SportsEventResponseDto.class)).thenReturn(responseDto);

        SportsEventResponseDto result = target.convertToResponseDto(sportsEvent);

        assertSame(responseDto, result);
        verify(modelMapper).map(sportsEvent, SportsEventResponseDto.class);
    }

    /**
     * {@link SportsEventMapper#convertAllToResponseDto(List)}
     */
    @Test
    void shouldConvertEntitiesToResponseList() {

        SportsEvent sportsEvent1 = new SportsEvent();
        SportsEvent sportsEvent2 = new SportsEvent();
        List<SportsEvent> entities = List.of(sportsEvent1, sportsEvent2);

        when(modelMapper.map(sportsEvent1, SportsEventResponseDto.class)).thenReturn(new SportsEventResponseDto());
        when(modelMapper.map(sportsEvent2, SportsEventResponseDto.class)).thenReturn(new SportsEventResponseDto());

        List<SportsEventResponseDto> result = target.convertAllToResponseDto(entities);

        assertEquals(2, result.size());
        verify(modelMapper, times(2)).map(any(SportsEvent.class), any());
    }
}

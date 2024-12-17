package sports.demoapi.controller.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sports.demoapi.controller.dto.SportsEventInputDto;
import sports.demoapi.controller.dto.SportsEventResponseDto;
import sports.demoapi.controller.mapper.Mapper;
import sports.demoapi.model.SportsEvent;

import java.util.List;

@Component
public class SportsEventMapper implements Mapper<SportsEvent, SportsEventInputDto, SportsEventResponseDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public SportsEventMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SportsEventResponseDto convertToResponseDto(SportsEvent sportsEvent) {
        return modelMapper.map(sportsEvent, SportsEventResponseDto.class);
    }

    @Override
    public SportsEvent convertToEntity(SportsEventInputDto sportsEventInputDto) {
        return modelMapper.map(sportsEventInputDto, SportsEvent.class);
    }

    @Override
    public List<SportsEventResponseDto> convertAllToResponseDto(List<SportsEvent> sportsEvents) {

        return sportsEvents.stream().map(this::convertToResponseDto).toList();
    }
}

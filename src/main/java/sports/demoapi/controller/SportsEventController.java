package sports.demoapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sports.demoapi.controller.dto.SportsEventInputDto;
import sports.demoapi.controller.dto.SportsEventResponseDto;
import sports.demoapi.controller.exception.ResourceNotFoundException;
import sports.demoapi.controller.mapper.impl.SportsEventMapper;
import sports.demoapi.model.SportsEvent;
import sports.demoapi.service.SportsEventService;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/sportsevent")
public class SportsEventController {

    private final SportsEventService service;
    private final SportsEventMapper mapper;

    @Autowired
    public SportsEventController(SportsEventService service, SportsEventMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("")
    public SportsEventResponseDto addSportsEvent(@RequestBody SportsEventInputDto inputDto) {
        SportsEvent sportsEvent = mapper.convertToEntity(inputDto);
        sportsEvent = service.save(sportsEvent);
        return mapper.convertToResponseDto(sportsEvent);
    }

    @GetMapping("/{eventId}")
    public SportsEventResponseDto getSportsEvent(@PathVariable("eventId") long eventId) {
        Optional<SportsEvent> optionalSportsEvent = service.getById(eventId);
        return optionalSportsEvent.map(mapper::convertToResponseDto).orElse(null);
    }

    @PutMapping("/{eventId}")
    public SportsEventResponseDto updateSportsEvent(@PathVariable("eventId") long eventId, @RequestBody SportsEventInputDto inputDto) {

        SportsEvent sportsEvent = service.getById(eventId).orElseThrow(() -> new ResourceNotFoundException("No event exists with id: " + eventId));

        if (Objects.nonNull(inputDto.getType())) {
            sportsEvent.setType(inputDto.getType());
        }

        if (Objects.nonNull(inputDto.getStatus())) {
            sportsEvent.setStatus(inputDto.getStatus());
        }

        sportsEvent = service.save(sportsEvent);
        return mapper.convertToResponseDto(sportsEvent);
    }
}

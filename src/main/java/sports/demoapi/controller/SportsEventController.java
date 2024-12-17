package sports.demoapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sports.demoapi.controller.dto.SportsEventInputDto;
import sports.demoapi.controller.dto.SportsEventResponseDto;
import sports.demoapi.controller.mapper.impl.SportsEventMapper;
import sports.demoapi.model.SportsEvent;
import sports.demoapi.service.SportsEventService;

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
}

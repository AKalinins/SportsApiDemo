package sports.demoapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sports.demoapi.controller.dto.SportsEventInputDto;
import sports.demoapi.controller.dto.SportsEventResponseDto;
import sports.demoapi.controller.exception.ResourceNotFoundException;
import sports.demoapi.controller.mapper.impl.SportsEventMapper;
import sports.demoapi.controller.validator.SportsEventStatusChangeValidator;
import sports.demoapi.model.EventStatus;
import sports.demoapi.model.SportsEvent;
import sports.demoapi.service.SportsEventService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class SportsEventController {

    private final SportsEventService service;
    private final SportsEventMapper mapper;
    private final SportsEventStatusChangeValidator statusValidator;

    @Autowired
    public SportsEventController(SportsEventService service, SportsEventMapper mapper,
                                 SportsEventStatusChangeValidator statusValidator) {
        this.service = service;
        this.mapper = mapper;
        this.statusValidator = statusValidator;
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
        return optionalSportsEvent.map(mapper::convertToResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("No event exists with id: " + eventId));
    }

    @GetMapping("/events")
    public List<SportsEventResponseDto> getSportEvents(@RequestParam(value = "type", required = false) String type,
                                                       @RequestParam(value = "status", required = false) EventStatus status) {
        List<SportsEvent> listOfEvents = service.getBy(type, status);
        return listOfEvents.stream().map(mapper::convertToResponseDto).toList();
    }

    @PutMapping("/{eventId}/{status}")
    public SportsEventResponseDto updateSportsEventStatus(@PathVariable("eventId") long eventId, @PathVariable("status") EventStatus newStatus) {

        SportsEvent sportsEvent = service.getById(eventId).orElseThrow(() -> new ResourceNotFoundException("No event exists with id: " + eventId));

        if (statusValidator.isValidChange(sportsEvent.getStatus(), newStatus, sportsEvent.getStartTime())) {
            sportsEvent.setStatus(newStatus);
        } else {
            throw new ResponseStatusException(HttpStatus.ACCEPTED, "Cannot change the status as requested for event with id: " + eventId);
        }

        sportsEvent = service.save(sportsEvent);
        return mapper.convertToResponseDto(sportsEvent);
    }
}

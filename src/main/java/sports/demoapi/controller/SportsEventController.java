package sports.demoapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sports.demoapi.controller.dto.SportsEventInputDto;
import sports.demoapi.controller.dto.SportsEventResponseDto;
import sports.demoapi.controller.mapper.impl.SportsEventMapper;
import sports.demoapi.model.SportsEvent;
import sports.demoapi.service.SportsEventService;

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
        try {
            sportsEvent = service.save(sportsEvent);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }
        return mapper.convertToResponseDto(sportsEvent);
    }
}

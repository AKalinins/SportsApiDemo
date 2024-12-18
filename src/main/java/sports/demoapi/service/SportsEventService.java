package sports.demoapi.service;

import sports.demoapi.model.EventStatus;
import sports.demoapi.model.SportsEvent;

import java.util.List;
import java.util.Optional;

public interface SportsEventService {

    SportsEvent save(SportsEvent sportsEvent);
    Optional<SportsEvent> getById(long id);
    List<SportsEvent> getBy(String eventType, EventStatus eventStatus);
}

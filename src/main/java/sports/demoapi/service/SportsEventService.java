package sports.demoapi.service;

import sports.demoapi.model.SportsEvent;

import java.util.Optional;

public interface SportsEventService {

    SportsEvent save(SportsEvent sportsEvent);
    Optional<SportsEvent> getById(long id);
}

package sports.demoapi.service;

import org.springframework.stereotype.Service;
import sports.demoapi.model.SportsEvent;

import java.util.Optional;

@Service
public interface SportsEventService {

    SportsEvent save(SportsEvent sportsEvent);
    Optional<SportsEvent> getById(long id);
}

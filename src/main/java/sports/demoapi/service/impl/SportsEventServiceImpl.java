package sports.demoapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sports.demoapi.model.EventStatus;
import sports.demoapi.model.SportsEvent;
import sports.demoapi.repository.SportsEventRepository;
import sports.demoapi.service.SportsEventService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SportsEventServiceImpl implements SportsEventService {

    private final SportsEventRepository repository;

    @Autowired
    public SportsEventServiceImpl(SportsEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public SportsEvent save(SportsEvent sportsEvent) {
        return repository.save(sportsEvent);
    }

    @Override
    public Optional<SportsEvent> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<SportsEvent> getBy(String eventType, EventStatus eventStatus) {

        if (Objects.nonNull(eventType) && Objects.nonNull(eventStatus)) {
            return repository.findByTypeAndStatus(eventType, eventStatus);
        } else if (Objects.nonNull(eventType)) {
            return repository.findByType(eventType);
        } else if (Objects.nonNull(eventStatus)) {
            return repository.findByStatus(eventStatus);
        } else {
            return repository.findAll();
        }
    }
}

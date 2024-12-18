package sports.demoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sports.demoapi.model.EventStatus;
import sports.demoapi.model.SportsEvent;

import java.util.List;

@Repository
public interface SportsEventRepository extends JpaRepository<SportsEvent, Long> {

    List<SportsEvent> findByTypeAndStatus(String eventType, EventStatus eventStatus);
    List<SportsEvent> findByStatus(EventStatus eventStatus);
    List<SportsEvent> findByType(String eventType);
}

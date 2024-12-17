package sports.demoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sports.demoapi.model.SportsEvent;

@Repository
public interface SportsEventRepository extends JpaRepository<SportsEvent, Long> {
}

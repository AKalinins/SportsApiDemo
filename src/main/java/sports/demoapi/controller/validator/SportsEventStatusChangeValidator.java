package sports.demoapi.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sports.demoapi.model.EventStatus;
import sports.demoapi.util.DateProvider;

import java.time.LocalDateTime;

@Component
public class SportsEventStatusChangeValidator {

    private final DateProvider dateProvider;

    @Autowired
    public SportsEventStatusChangeValidator(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    public boolean isValidChange(EventStatus existingStatus, EventStatus newStatus, LocalDateTime eventTime) {

        if (EventStatus.FINISHED.equals(existingStatus)) return false;

        if (EventStatus.ACTIVE.equals(newStatus) && eventTime.isBefore(dateProvider.getCurrentDateTime())) return false;

        if (EventStatus.INACTIVE.equals(existingStatus) && EventStatus.FINISHED.equals(newStatus)) return false;

        return true;
    }
}

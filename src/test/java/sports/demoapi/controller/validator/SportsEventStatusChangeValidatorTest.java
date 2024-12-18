package sports.demoapi.controller.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sports.demoapi.model.EventStatus;
import sports.demoapi.util.DateProvider;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SportsEventStatusChangeValidatorTest {

    @InjectMocks
    private SportsEventStatusChangeValidator target;

    @Mock
    private DateProvider dateProvider;

    /**
     * {@link SportsEventStatusChangeValidator#isValidChange(EventStatus, EventStatus, LocalDateTime)}
     */
    @Test
    void shouldRejectFinishedEventChange() {

        EventStatus existingStatus = EventStatus.FINISHED;
        EventStatus newStatus = EventStatus.ACTIVE;

        boolean result = target.isValidChange(existingStatus, newStatus, null);
        assertFalse(result);
    }

    /**
     * {@link SportsEventStatusChangeValidator#isValidChange(EventStatus, EventStatus, LocalDateTime)}
     */
    @Test
    void shouldRejectStartedEventChange() {

        EventStatus existingStatus = EventStatus.INACTIVE;
        EventStatus newStatus = EventStatus.ACTIVE;
        LocalDateTime eventTime = LocalDateTime.of(1990, 1, 1, 10, 0, 0);
        when(dateProvider.getCurrentDateTime()).thenReturn(LocalDateTime.of(2023, 1, 1, 10, 0, 0));

        boolean result = target.isValidChange(existingStatus, newStatus, eventTime);
        assertFalse(result);
    }

    /**
     * {@link SportsEventStatusChangeValidator#isValidChange(EventStatus, EventStatus, LocalDateTime)}
     */
    @Test
    void shouldRejectInactiveToFinishedEventChange() {

        EventStatus existingStatus = EventStatus.INACTIVE;
        EventStatus newStatus = EventStatus.FINISHED;

        boolean result = target.isValidChange(existingStatus, newStatus, null);
        assertFalse(result);
    }

    /**
     * {@link SportsEventStatusChangeValidator#isValidChange(EventStatus, EventStatus, LocalDateTime)}
     */
    @Test
    void shouldAllowEventChange() {

        EventStatus existingStatus = EventStatus.INACTIVE;
        EventStatus newStatus = EventStatus.ACTIVE;
        LocalDateTime eventTime = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        when(dateProvider.getCurrentDateTime()).thenReturn(LocalDateTime.of(2023, 1, 1, 10, 0, 0));

        boolean result = target.isValidChange(existingStatus, newStatus, eventTime);
        assertTrue(result);
    }
}

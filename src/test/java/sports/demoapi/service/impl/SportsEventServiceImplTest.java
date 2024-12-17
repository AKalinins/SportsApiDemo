package sports.demoapi.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sports.demoapi.model.SportsEvent;
import sports.demoapi.repository.SportsEventRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SportsEventServiceImplTest {

    @Mock
    private SportsEventRepository repository;

    @InjectMocks
    private SportsEventServiceImpl target;

    /**
     * {@link SportsEventServiceImpl#save(SportsEvent)}
     */
    @Test
    void shouldSaveEventToRepository() {

        SportsEvent event = new SportsEvent();

        when(repository.save(event)).thenReturn(event);

        SportsEvent result = target.save(event);

        verify(repository).save(event);
        assertSame(event, result);
    }

    /**
     * {@link SportsEventServiceImpl#getById(long)}
     */
    @Test
    void shouldGetEventById() {

        SportsEvent event = new SportsEvent();
        event.setId(3L);

        Optional<SportsEvent> result = target.getById(1L);
        assertFalse(result.isPresent());

        doReturn(Optional.of(event)).when(repository).findById(event.getId());
        result = target.getById(3L);
        assertTrue(result.isPresent());
        assertSame(event, result.get());

        verify(repository, times(2)).findById(anyLong());
    }
}

package sports.demoapi.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateProvider {

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}

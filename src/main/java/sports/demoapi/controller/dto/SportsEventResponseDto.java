package sports.demoapi.controller.dto;

import lombok.Getter;
import lombok.Setter;
import sports.demoapi.model.EventStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class SportsEventResponseDto {

    private long id;
    private String name;
    private String type;
    private EventStatus status;
    private LocalDateTime startTime;
}

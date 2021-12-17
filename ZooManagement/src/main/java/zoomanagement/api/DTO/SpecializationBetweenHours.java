package zoomanagement.api.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpecializationBetweenHours {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

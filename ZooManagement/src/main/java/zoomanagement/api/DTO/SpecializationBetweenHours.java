package zoomanagement.api.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class SpecializationBetweenHours {
    @NotBlank(message = "Name field is mandatory")
    @Size(min = 3, max = 30, message = "Name field should have between 3 and 30 characters.")
    private String name;

    @NotBlank(message = "Start time field is mandatory")
    private LocalDateTime startTime;

    @NotBlank(message = "End time field is mandatory")
    private LocalDateTime endTime;
}

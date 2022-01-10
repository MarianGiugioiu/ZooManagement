package zoomanagement.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecializationBetweenHours {
    @NotBlank(message = "Name field is mandatory")
    @Size(min = 3, max = 30, message = "Name field should have between 3 and 30 characters.")
    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}

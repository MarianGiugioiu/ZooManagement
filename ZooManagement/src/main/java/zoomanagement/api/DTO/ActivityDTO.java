package zoomanagement.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.domain.Pen;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDTO {
    @NotBlank(message = "Name field is mandatory")
    @Size(min = 3, max = 30, message = "Name field should have between 3 and 30 characters.")
    private String name;

    @NotNull(message = "Start time field is mandatory")
    private LocalDateTime startTime;

    @NotNull(message = "End time field is mandatory")
    private LocalDateTime endTime;

    @NotBlank(message = "Action field is mandatory")
    @Size(min = 3, max = 100, message = "Action field should have between 3 and 100 characters.")
    private String action;

    @NotBlank(message = "Pen name field is mandatory")
    @Size(min = 3, max = 30, message = "Pen name field should have between 3 and 30 characters.")
    private String penName;

    @NotNull(message = "Employee names field should not be null.")
    private List<String> employeeNames;
}

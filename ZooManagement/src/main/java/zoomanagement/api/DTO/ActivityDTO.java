package zoomanagement.api.DTO;

import lombok.Data;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.domain.Pen;

import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActivityDTO {
    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String action;

    private String penName;

    private List<String> employeeNames;
}

package zoomanagement.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zoomanagement.api.domain.Employee;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaySchedule {
    private Integer day;
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public String toString() {
        return "DaySchedule{" +
                "day=" + day +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

}

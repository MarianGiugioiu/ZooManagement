package zoomanagement.api.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import zoomanagement.api.serializer.AnimalListSerializer;
import zoomanagement.api.serializer.EmployeeListSerializer;
import zoomanagement.api.serializer.PenSerializer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Type(type="uuid-char")
    private UUID id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String action;

    private String status;

    @ManyToOne
    @JsonSerialize(using = PenSerializer.class)
    private Pen pen;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "works_in",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    @JsonSerialize(using = EmployeeListSerializer.class)
    private List<Employee> employees;
}

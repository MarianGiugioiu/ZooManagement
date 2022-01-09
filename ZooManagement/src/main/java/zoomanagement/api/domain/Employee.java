package zoomanagement.api.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import zoomanagement.api.DTO.DaySchedule;
import zoomanagement.api.serializer.ActivityListSerializer;
import zoomanagement.api.serializer.AnimalListSerializer;
import zoomanagement.api.serializer.SpeciesListSerializer;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
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

    private String profession;

    private String schedule;

    @ManyToMany
    @JoinTable(
            name = "specializations",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "species_id")
    )
    @JsonSerialize(using = SpeciesListSerializer.class)
    private List<Species> specializations;

    @ManyToMany
    @JoinTable(
            name = "works_in",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    @JsonSerialize(using = ActivityListSerializer.class)
    private List<Activity> activities;
}

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

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animal")
public class Animal {
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

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String age;

    private String sex;

    private String status;

    private String peculiarities;

    @ManyToOne
    private Species species;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval=true)
    private Diet diet;

    @ManyToOne
    private Pen pen;

    @ManyToMany()
    @JoinTable(name="parents",
            joinColumns={@JoinColumn(name="child_id")},
            inverseJoinColumns={@JoinColumn(name="parent_id")})
    @JsonSerialize(using = AnimalListSerializer.class)
    private List<Animal> parents;

    @ManyToMany()
    @JoinTable(name="parents",
            joinColumns={@JoinColumn(name="parent_id")},
            inverseJoinColumns={@JoinColumn(name="child_id")})
    @JsonSerialize(using = AnimalListSerializer.class)
    private List<Animal> children;
}

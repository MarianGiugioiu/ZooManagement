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
import zoomanagement.api.serializer.DietSerializer;
import zoomanagement.api.serializer.PenSerializer;

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

    private String name;

    private String age;

    private String sex;

    private String status;

    private String peculiarities;

    @ManyToOne
    private Species species;

    @OneToOne(cascade = CascadeType.MERGE, orphanRemoval=true)
    @JsonSerialize(using = DietSerializer.class)
    private Diet diet;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonSerialize(using = PenSerializer.class)
    private Pen pen;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="parents",
            joinColumns={@JoinColumn(name="child_id")},
            inverseJoinColumns={@JoinColumn(name="parent_id")})
    @JsonSerialize(using = AnimalListSerializer.class)
    private List<Animal> parents;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="parents",
            joinColumns={@JoinColumn(name="parent_id")},
            inverseJoinColumns={@JoinColumn(name="child_id")})
    @JsonSerialize(using = AnimalListSerializer.class)
    private List<Animal> children;

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                "\n name='" + name + '\'' +
                "\n age='" + age + '\'' +
                "\n sex='" + sex + '\'' +
                "\n status='" + status + '\'' +
                "\n peculiarities='" + peculiarities + '\'' +
                "\n species=" + species +
                "\n diet=" + diet +
                "\n pen=" + pen +
                "\n parents=" + parents +
                "\n children=" + children +
                '}';
    }
}

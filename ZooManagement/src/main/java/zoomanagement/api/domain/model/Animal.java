package zoomanagement.api.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import zoomanagement.api.serializer.AnimalListSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
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

    @ManyToOne
    private Species species;

    private String age;

    private String sex;

    private String status;

    private String peculiarities;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval=true)
    private Diet diet;

    @ManyToOne
    private Pen pen;

    @ManyToMany()
    @JoinTable(name="parents",
            joinColumns={@JoinColumn(name="child_id")},
            inverseJoinColumns={@JoinColumn(name="parent_id")})
    @JsonSerialize(using = AnimalListSerializer.class)
    private List<Animal> parents = new ArrayList<>();

    @ManyToMany()
    @JoinTable(name="parents",
            joinColumns={@JoinColumn(name="parent_id")},
            inverseJoinColumns={@JoinColumn(name="child_id")})
    @JsonSerialize(using = AnimalListSerializer.class)
    private List<Animal> children = new ArrayList<>();
}

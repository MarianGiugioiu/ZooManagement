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
import zoomanagement.api.serializer.SpeciesSerializer;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pen")
public class Pen implements Comparable<Pen>{
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

    private String location;

    private Double surface;

    private String status;

    private String description;

    @ManyToOne
    @JsonSerialize(using = SpeciesSerializer.class)
    private Species species;

    @OneToMany(mappedBy = "pen", cascade = CascadeType.MERGE)
    @JsonSerialize(using = AnimalListSerializer.class)
    private List<Animal> animals;

    @Override
    public int compareTo(Pen pen) {
        String[] pos1 = this.location.split(":");
        String[] pos2 = pen.location.split(":");
        Double x1 = Double.parseDouble(pos1[0]);
        Double x2 = Double.parseDouble(pos1[1]);
        Double y1 = Double.parseDouble(pos2[0]);
        Double y2 = Double.parseDouble(pos2[1]);
        Double val1 = Math.sqrt(x1 * x1 + x2 * x2);
        Double val2 = Math.sqrt(y1 * y1 + y2 * y2);

        return (int) Math.signum(val1 - val2);
    }
}

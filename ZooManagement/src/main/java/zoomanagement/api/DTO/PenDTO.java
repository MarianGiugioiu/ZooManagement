package zoomanagement.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zoomanagement.api.domain.Species;

import javax.persistence.ManyToOne;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenDTO {
    private String name;

    private String location;

    private String surface;

    private String description;

    private String speciesName;
}

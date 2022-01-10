package zoomanagement.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zoomanagement.api.domain.Species;
import zoomanagement.api.validator.ValidateFoodList;
import zoomanagement.api.validator.ValidateLocation;

import javax.persistence.ManyToOne;
import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenDTO {
    @NotBlank(message = "Name field is mandatory.")
    @Size(min = 3, max = 30, message = "Name field should have between 3 and 30 characters.")
    private String name;

    @NotBlank(message = "Location field is mandatory.")
    @ValidateLocation(message = "Location should contain two double values separated by a colon.")
    private String location;

    @NotNull(message = "Surface field is mandatory.")
    @DecimalMin("0.0")
    @DecimalMax(("1000.0"))
    private Double surface;

    @NotBlank(message = "Description field is mandatory.")
    @ValidateFoodList(message = "Description should contain a list of #<habitat property># elements separated by commas without spaces in between.")
    private String description;

    @NotBlank(message = "Species mame field is mandatory.")
    @Size(min = 3, max = 30, message = "Species mame should have between 3 and 30 characters.")
    private String speciesName;
}

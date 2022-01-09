package zoomanagement.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zoomanagement.api.domain.Diet;
import zoomanagement.api.validator.ValidateString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BabyAnimal {
    @NotBlank(message = "Name field is mandatory.")
    @Size(min = 3, max = 30, message = "Name field should have between 3 and 30 characters.")
    private String name;

    @Size(min = 2, max = 2, message = "The list of parents should contain exactly 2 nullable elements.")
    public List<String> parents;

    @NotBlank(message = "Sex field is mandatory.")
    @ValidateString(acceptedValues={"male", "female"}, message="Sex field should be male or female.")
    public String sex;

    @NotBlank(message = "Peculiarities field is mandatory.")
    @Size(min = 3, max = 200, message = "Peculiarities field should have between 3 and 200 characters.")
    public String peculiarities;

    @NotNull(message = "Diet field should not be null.")
    public DietDTO diet;
}

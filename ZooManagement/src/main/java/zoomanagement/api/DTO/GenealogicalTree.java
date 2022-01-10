package zoomanagement.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenealogicalTree {
    @NotBlank(message = "Name field is mandatory.")
    @Size(min = 3, max = 30, message = "Name field should have between 3 and 30 characters.")
    private String name;

    //@Size(min = 2, max = 2, message = "The list of parents should contain exactly 2 nullable elements.")
    public List<String> parents;
}

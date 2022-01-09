package zoomanagement.api.DTO;

import lombok.Data;
import zoomanagement.api.validator.ValidateDietSchedule;
import zoomanagement.api.validator.ValidateFoodList;

import javax.validation.constraints.NotNull;

@Data
public class DietDTO {
    @NotNull(message = "Recommendations field should not be null.")
    @ValidateFoodList(message = "Recommendations should contain a list of #<food># elements separated by commas without spaces in between.")
    private String recommendations;

    @NotNull(message = "Schedule field should not be null.")
    @ValidateDietSchedule(message = "Schedule should be a list of elements of type <hours>:<minutes> separated by commas.")
    private String schedule;

    @NotNull(message = "Preferences field should not be null.")
    @ValidateFoodList(message = "Preferences should contain a list of #<food># elements separated by commas without spaces in between.")
    private String preferences;
}

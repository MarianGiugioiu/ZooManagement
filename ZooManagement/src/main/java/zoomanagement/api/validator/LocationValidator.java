package zoomanagement.api.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationValidator implements ConstraintValidator<ValidateLocation, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String[] list = s.split(":");
        if (list.length != 2) {
            return false;
        }
        try {
            for (String value : list) {
                Double.parseDouble(value);
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}

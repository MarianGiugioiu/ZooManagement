package zoomanagement.api.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DietScheduleValidator implements ConstraintValidator<ValidateDietSchedule, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String[] scheduleParts = s.split("-");
        if (scheduleParts.length < 1 || scheduleParts.length > 5) return false;
        for (String schedulePart : scheduleParts) {
            String[] time = schedulePart.split(":");
            if (time.length != 2) return false;
            try {
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                if (hour < 0 || hour > 24 || minute < 0 || minute > 60) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}

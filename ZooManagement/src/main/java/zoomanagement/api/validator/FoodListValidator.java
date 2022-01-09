package zoomanagement.api.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FoodListValidator implements ConstraintValidator<ValidateFoodList, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String[] foods = s.split(",");
        for (String food : foods) {
            if (food.length() < 4 || food.length() > 32 || food.charAt(0) != '#' || food.charAt(food.length() - 1) != '#') {
                return false;
            }
        }
        return true;
    }
}

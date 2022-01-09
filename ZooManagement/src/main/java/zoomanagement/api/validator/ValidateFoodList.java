package zoomanagement.api.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FoodListValidator.class)
@Documented
public @interface ValidateFoodList {

    String message() default "Only letters required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

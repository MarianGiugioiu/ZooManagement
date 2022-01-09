package zoomanagement.api.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringValidator.class)
@Documented
public @interface ValidateString {

    String[] acceptedValues();

    String message() default "{Value is not valid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

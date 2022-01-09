package zoomanagement.api.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DietScheduleValidator.class)
@Documented
public @interface ValidateDietSchedule {

    String message() default "Only letters required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

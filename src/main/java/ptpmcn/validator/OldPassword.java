package ptpmcn.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OldPasswordValidator.class)
public @interface OldPassword {
	String message() default "Old password not true";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

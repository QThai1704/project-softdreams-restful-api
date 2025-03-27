package softdreams.website.project_softdreams_restful_api.util.anotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import softdreams.website.project_softdreams_restful_api.util.validation.CustomStrongValidator;

@Constraint(validatedBy = CustomStrongValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER}) 
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrongPassword {
    String message() default "Định dạng mật khẩu sai"; 

    Class<?>[] groups() default {}; 

    Class<? extends Payload>[] payload() default {}; 

    String domain() default "";
}

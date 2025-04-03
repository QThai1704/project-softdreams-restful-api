package softdreams.website.project_softdreams_restful_api.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import softdreams.website.project_softdreams_restful_api.exception.RegisterException;
import softdreams.website.project_softdreams_restful_api.repository.UserRepository;
import softdreams.website.project_softdreams_restful_api.util.anotation.EmailAno;

public class CustomEmailValidator implements ConstraintValidator<EmailAno, String>{


    private UserRepository userRepository;

    public CustomEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String allowedDomain;

    @Override
    public void initialize(EmailAno constraintAnnotation) {
        this.allowedDomain = constraintAnnotation.domain(); 
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            buildViolation(context, "Email không được trống");
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            buildViolation(context, "Email không hợp lệ");
            return false; 
        }

        if (!allowedDomain.isEmpty() && !email.endsWith("@" + allowedDomain)) {
            buildViolation(context, "Email phải có định dạng " + allowedDomain);
            return false;
        }

        if (email.contains("!@") || email.contains("#")) {
            buildViolation(context, "Email không được chứa ký tự đặc biệt !@#");
            return false;
        }
        
        return true;
    }
    
    private void buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}

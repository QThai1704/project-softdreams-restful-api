package softdreams.website.project_softdreams_restful_api.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
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
            return false; 
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            return false; 
        }

        if (!allowedDomain.isEmpty() && !email.endsWith("@" + allowedDomain)) {
            context.disableDefaultConstraintViolation(); 
            context.buildConstraintViolationWithTemplate(
                    "Email phải có tên miền @" + allowedDomain)
                   .addConstraintViolation();
            return false;
        }

        if (email.contains("!@") || email.contains("#")) {
            return false;
        }

        if(userRepository.existsByEmail(email)) {
            context.disableDefaultConstraintViolation(); 
            context.buildConstraintViolationWithTemplate(
                    "Email đã tồn tại")
                   .addConstraintViolation();
            return false;
        }
        
        return true;
    }
    
}

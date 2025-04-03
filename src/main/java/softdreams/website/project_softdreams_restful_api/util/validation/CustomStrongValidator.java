package softdreams.website.project_softdreams_restful_api.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import softdreams.website.project_softdreams_restful_api.util.anotation.StrongPassword;

public class CustomStrongValidator implements ConstraintValidator<StrongPassword, String>{

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        
        if (password == null || password.trim().isEmpty()) {
            return false; 
        }

        String regexp = "^(?=.*[A-Z])(?=.*[0-9])[\\S]{8,}$";
        if(!password.matches(regexp)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Mật khẩu phải có ít nhất 8 ký tự trong đó một ký tự viết hoa, ký tự số và không chứa khoảng trắng.")
                    .addConstraintViolation();
            return false;
        }

        if(password.length() < 8){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Mật khẩu phải có ít nhất 8 ký tự.")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
    
}

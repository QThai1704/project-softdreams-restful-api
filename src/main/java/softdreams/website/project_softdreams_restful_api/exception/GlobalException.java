package softdreams.website.project_softdreams_restful_api.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import softdreams.website.project_softdreams_restful_api.dto.response.ResGlobal;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
        NullPointerException.class,
        IllegalArgumentException.class,
        CustomException.class
    })
    public ResponseEntity<ResGlobal<Object>> checkExists(Exception ex) {
        ResGlobal<Object> res = new ResGlobal<Object>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setError("Yêu cầu lỗi");
        String message = ex.getMessage();
        // int start = message.indexOf("messageTemplate=\'");
        // int end = message.indexOf("\'}");
        // String msgCode = message.substring(start + 16, end);
        res.setMessage(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {
        MethodArgumentNotValidException.class,
    })
    public ResponseEntity<ResGlobal<Object>> checkValidator(MethodArgumentNotValidException ex) {
        ResGlobal<Object> res = new ResGlobal<Object>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String errorMessage = error.getDefaultMessage();
            res.setMessage(errorMessage);
        }
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setError("Trường đầu vào không hợp lệ");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {
        RegisterException.class,
    })
    public ResponseEntity<ResGlobal<Object>> checkValidator(RegisterException ex) {
        ResGlobal<Object> res = new ResGlobal<Object>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setError("Đăng ký không thành công");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}

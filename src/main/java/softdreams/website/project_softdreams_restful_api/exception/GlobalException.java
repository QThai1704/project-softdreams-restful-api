package softdreams.website.project_softdreams_restful_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import softdreams.website.project_softdreams_restful_api.dto.response.ResGlobal;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
        RuntimeException.class
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
}

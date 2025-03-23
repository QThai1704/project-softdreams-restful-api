package softdreams.website.project_softdreams_restful_api.util;

import java.sql.Timestamp;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import softdreams.website.project_softdreams_restful_api.dto.response.ResGlobal;

@ControllerAdvice
public class FommattedResSuccess implements ResponseBodyAdvice<Object>{

    @Override
    @Nullable
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        if(body instanceof byte[]){
            return body;
        }
        
        ResGlobal<Object> res = new ResGlobal<Object>();
        res.setTimestamp(new Timestamp(System.currentTimeMillis()));
        res.setStatus(status);
        if (body instanceof String) {
            return body;
        }

        if (status >= 400) {
            return body;
        } else {
            res.setData(body);
            ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
            res.setMessage(apiMessage != null ? apiMessage.message() : "Gọi API thành công.");
        }
        return res;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
       return true;
    }
    
}

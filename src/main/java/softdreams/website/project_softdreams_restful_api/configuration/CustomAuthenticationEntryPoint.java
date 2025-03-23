package softdreams.website.project_softdreams_restful_api.configuration;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import softdreams.website.project_softdreams_restful_api.dto.response.ResGlobal;

// Xử lý khi token không hợp lệ
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{
    // Đối tượng xử lý token trái phép
    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        this.delegate.commence(request, response, authException);
        response.setContentType("application/problem+json; charset=UTF-8");
        
        ResGlobal<Object> res = new ResGlobal<Object>();
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        String errorMessage = Optional.ofNullable(authException.getCause())
                .map(Throwable::getMessage)
                .orElse(authException.getMessage());
        res.setError(errorMessage);
        res.setMessage("Token không hợp lệ (hết hạn, không đúng định dạng hoặc không tồn tại)");
        mapper.writeValue(response.getWriter(), res);
    }
}

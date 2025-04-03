package softdreams.website.project_softdreams_restful_api.configuration;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
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
        if (authException instanceof BadCredentialsException) {
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.setError("Đăng nhập không thành công");
            res.setMessage("Tài khoản hoặc mật khẩu không đúng");
        } else if (authException instanceof InsufficientAuthenticationException) {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setError("Token không hợp lệ");
            res.setMessage("Token không hợp lệ (hết hạn, sai định dạng hoặc không tồn tại)");
        } else if (authException instanceof CredentialsExpiredException) {
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.setError("Token đã hết hạn");
            res.setMessage("Token của bạn đã hết hạn, vui lòng đăng nhập lại");
        } else {
            // Các trường hợp khác
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setError("Lỗi xác thực");
            res.setMessage("Xác thực thất bại, vui lòng kiểm tra lại");
        }
        mapper.writeValue(response.getWriter(), res);
    }
}

package softdreams.website.project_softdreams_restful_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import softdreams.website.project_softdreams_restful_api.util.anotation.EmailAno;
import softdreams.website.project_softdreams_restful_api.util.anotation.StrongPassword;

@Getter
@Setter
public class LoginReq {
    @NotBlank(message = "Email không được trống")
    @EmailAno(message = "Email không hợp lệ", domain = "gmail.com")
    private String email;
    @NotBlank(message = "Mật khẩu không được trống")
    @StrongPassword(message = "Mật khẩu không đủ mạnh")
    private String password;
}

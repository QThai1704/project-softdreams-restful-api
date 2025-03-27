package softdreams.website.project_softdreams_restful_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import softdreams.website.project_softdreams_restful_api.util.anotation.EmailAno;
import softdreams.website.project_softdreams_restful_api.util.anotation.StrongPassword;

@Getter
@Setter
public class RegisterReq {
    @NotBlank(message = "Tên không được để trống")
    private String lastName;
    @NotBlank(message = "Họ và tên đệm không được trống")
    private String firstName;
    @NotBlank(message = "Email không được trống")
    @EmailAno(message = "Email không hợp lệ", domain = "gmail.com")
    private String email;
    @StrongPassword(message = "Mật khẩu không đủ mạnh")
    @NotBlank(message = "Mật khẩu không trống")
    private String password;
    @NotBlank(message = "Xác nhận mật khẩu không được trống")
    private String confirmPassword;
}

package softdreams.website.project_softdreams_restful_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import softdreams.website.project_softdreams_restful_api.util.anotation.EmailAno;
import softdreams.website.project_softdreams_restful_api.util.anotation.StrongPassword;

@Getter
@Setter
public class UserReqCreate {
    @NotBlank(message = "Họ và tên không được trống")
    @Size(min = 5, max = 100, message = "Họ và tên phải trên 5 ký tự và không quá 100 ký tự")
    private String fullName;

    @NotBlank(message = "Email không được trống")
    @EmailAno(message = "Định dạng email không đúng", domain = "gmail.com")
    private String email;

    @NotBlank(message = "Mật khẩu không được trống")
    @StrongPassword(message = "Mật khẩu không đủ mạnh")
    private String password;

    @NotBlank(message = "Số điện thoại không được trống")
    @Pattern(regexp = "^(\\+84|0)[35789][0-9]{8}$", message = "Định dạng số điện thoại không đúng")
    private String phone;

    private String address;
    private String avatar;

    @NotNull(message = "Quyền không được trống")
    @Pattern(regexp = "^(ADMIN|USER)$", message = "Quyền của người dùng không hợp lệ")
    private String role;
}

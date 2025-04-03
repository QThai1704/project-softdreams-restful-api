package softdreams.website.project_softdreams_restful_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReqUpdate {
    @NotNull(message = "Id is required")
    private long id;

    @NotBlank(message = "Họ và tên không được trống")
    @Size(min = 5, max = 100, message = "Full name must be between 5 and 100 characters")
    private String fullName;

    private String email;

    @NotBlank(message = "Số điện thoại không được trống")
    @Pattern(regexp = "^(\\+84|0)[35789][0-9]{8}$", message = "Phone is not format")
    private String phone;

    private String address;
    private String avatar;

    @NotNull(message = "Role không được trống")
    @Pattern(regexp = "^(ADMIN|USER)$", message = "Role phải là ADMIN hoặc USER")
    private String role;
}

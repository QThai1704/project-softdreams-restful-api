package softdreams.website.project_softdreams_restful_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import softdreams.website.project_softdreams_restful_api.util.anotation.EmailAno;

@Getter
@Setter
public class UserReqUpdate {
    @NotNull(message = "Id is required")
    private long id;

    @NotBlank(message = "Full name is required")
    @Size(min = 5, max = 100, message = "Full name must be between 5 and 100 characters")
    private String fullName;

    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^(\\+84|0)[35789][0-9]{8}$", message = "Phone is not format")
    private String phone;

    private String address;
    private String avatar;

    @NotNull(message = "Role is required")
    @Pattern(regexp = "^(ADMIN|USER)$", message = "Role must be either ADMIN or USER")
    private String role;
}

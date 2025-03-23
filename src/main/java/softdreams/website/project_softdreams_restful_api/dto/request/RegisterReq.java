package softdreams.website.project_softdreams_restful_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterReq {
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private String confirmPassword;
}

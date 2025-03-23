package softdreams.website.project_softdreams_restful_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReq {
    private String email;
    private String password;
}

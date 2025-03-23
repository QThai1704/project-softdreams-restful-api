package softdreams.website.project_softdreams_restful_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReqCreate {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String avatar;
    private String role;
}

package softdreams.website.project_softdreams_restful_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReqUpdate {
    private long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private String role;
}

package softdreams.website.project_softdreams_restful_api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRes {
    private long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private Role role;

    @Getter
    @Setter
    public static class Role {
        private String name;
    }
}

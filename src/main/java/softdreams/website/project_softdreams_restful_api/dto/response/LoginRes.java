package softdreams.website.project_softdreams_restful_api.dto.response;

import lombok.Getter;
import lombok.Setter;
import softdreams.website.project_softdreams_restful_api.domain.Role;

@Getter
@Setter
public class LoginRes {
    private LoginUser user;
    private String accessToken;

    @Getter
    @Setter
    public static class LoginUser {
        private long id;
        private String email;
        private Role role;
    }
}

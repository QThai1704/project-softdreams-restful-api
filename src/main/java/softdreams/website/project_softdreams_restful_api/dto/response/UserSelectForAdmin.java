package softdreams.website.project_softdreams_restful_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSelectForAdmin {
    private long id;
    private String fullName;
    private String email;
}

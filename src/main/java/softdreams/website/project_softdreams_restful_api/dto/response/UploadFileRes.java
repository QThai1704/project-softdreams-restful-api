package softdreams.website.project_softdreams_restful_api.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileRes {
    private String fileName;
    private Instant uploadedAt;
}

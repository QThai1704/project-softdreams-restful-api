package softdreams.website.project_softdreams_restful_api.dto.response;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResGlobal<T> {
    private Timestamp timestamp;
    private String error;
    private String message;
    private int status;
    private T data;
}

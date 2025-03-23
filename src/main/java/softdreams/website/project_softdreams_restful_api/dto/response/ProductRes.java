package softdreams.website.project_softdreams_restful_api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRes {
    private long id;
    private String name;
    private double price;
    private String image;
    private String detailDesc;
    private String shortDesc;
    private long quantity;
}

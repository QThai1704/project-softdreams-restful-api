package softdreams.website.project_softdreams_restful_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartReq {
    private int quantity;
    private Long productId;
    private int sum;
}
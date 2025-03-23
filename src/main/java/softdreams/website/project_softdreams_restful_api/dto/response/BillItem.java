package softdreams.website.project_softdreams_restful_api.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BillItem {
    private String nameProduct;
    private Double priceProduct;
    private Long quantity;
    private Double priceOrderDetail;
}

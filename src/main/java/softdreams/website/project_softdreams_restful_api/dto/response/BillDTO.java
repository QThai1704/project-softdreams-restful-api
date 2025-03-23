package softdreams.website.project_softdreams_restful_api.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BillDTO {
    private String receiverName;
    private Double totalPrice;
    private Double discount;
    private Double personPay;
    private List<BillItem> items;
}

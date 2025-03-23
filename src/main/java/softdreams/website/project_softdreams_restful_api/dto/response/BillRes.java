package softdreams.website.project_softdreams_restful_api.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BillRes {
    private double totalPrice;
    private String receiverName;
    private List<BillItem> billItems;
    private double discount = 0;
    private double payment = 0;
    private double personPay;
}

package softdreams.website.project_softdreams_restful_api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
    private String nameProduct;
    private int quantity;
    private double price;
    private double totalPrice;
}

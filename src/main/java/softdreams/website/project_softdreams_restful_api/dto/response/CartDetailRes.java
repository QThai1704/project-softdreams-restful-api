package softdreams.website.project_softdreams_restful_api.dto.response;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softdreams.website.project_softdreams_restful_api.domain.Product;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailRes {
    List<CartDetailList> cartDetailList;
    @JsonSerialize(using = ToStringSerializer.class)
    double total;
    CartRes cartRes;

    @Getter
    @Setter
    public static class CartDetailList {
        private long id;
        private long quantity;
        private double price;
        private Product product;
    }

    @Getter
    @Setter
    public static class CartRes {
        private long id;
        private int sum;
    }
}

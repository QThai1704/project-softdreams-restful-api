package softdreams.website.project_softdreams_restful_api.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import softdreams.website.project_softdreams_restful_api.util.constants.StatusEnum;

@Getter
@Setter
public class OrderRes {
    private long id;
    private String orderCode;
    private double totalPrice;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private StatusEnum status;
    private User_OrderRes user;
    private List<OrderDetail_OrderRes> orderDetails;

    @Getter
    @Setter
    public static class User_OrderRes {
        private String fullName;
        private String email;
    }

    @Getter
    @Setter
    public static class OrderDetail_OrderRes {
        private long id;
        private long quantity;
        private double price;
        private Product_OrderRes product;

        @Getter
        @Setter
        public static class Product_OrderRes {
            private String name;
            private double price;
            private String image;
        }
    }
}

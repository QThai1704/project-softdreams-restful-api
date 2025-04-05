package softdreams.website.project_softdreams_restful_api.service;

import java.util.List;

import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.ReceiverReq;
import softdreams.website.project_softdreams_restful_api.dto.response.CartDetailRes;
import softdreams.website.project_softdreams_restful_api.exception.CustomException;

public interface CartService {
    public Cart fetchByUser(User user);
    public CartDetailRes convertCartDetailRes(String email);
    public void handleUpdateCartBeforeCheckout(List<CartDetailRes.CartDetailList> cartDetailList) throws CustomException;
    public void handlePlaceOrder(User user, ReceiverReq receiverReq, int sum, String uuid);
    public void deleteCartProduct(long id);
}

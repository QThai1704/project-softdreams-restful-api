package softdreams.website.project_softdreams_restful_api.service;

import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.CartDetail;

public interface CartService {
    Cart initOrder(String email);
    Cart createCart(Cart cart);
    Cart updateCartDetailForCart(CartDetail cartDetail, String email);
    void deleteCart(long id);
    Cart fetchCartByUser(String email);
}

package softdreams.website.project_softdreams_restful_api.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.CartDetail;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.repository.CartRepository;
import softdreams.website.project_softdreams_restful_api.service.CartService;
import softdreams.website.project_softdreams_restful_api.service.UserService;

@Service
public class ICartService implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Override
    public Cart createCart(Cart cart) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createCart'");
    }

    @Override
    public Cart updateCartDetailForCart(CartDetail cartDetail, String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCartDetailForCart'");
    }

    @Override
    public void deleteCart(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCart'");
    }

    @Override
    public Cart fetchCartByUser(String email) {
        User user = this.userService.findUserByEmail(email).get();
        return this.cartRepository.findByUser(user);
    }

    @Override
    public Cart initOrder(String email) {
        Cart cart = new Cart();
        User user = this.userService.findUserByEmail(email).get();
        cart.setUser(user);
        return this.cartRepository.save(cart);
    }
    
}

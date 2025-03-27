package softdreams.website.project_softdreams_restful_api.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.CartDetail;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.response.CartDetailRes;
import softdreams.website.project_softdreams_restful_api.repository.CartRepository;
import softdreams.website.project_softdreams_restful_api.repository.UserRepository;
import softdreams.website.project_softdreams_restful_api.service.CartService;

@Service
public class ICartService implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Cart fetchByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    @Override
    public CartDetailRes convertCartDetailRes(String email) {
        User currentUser = this.userRepository.findByEmail(email).get();
        Cart cart = this.cartRepository.findByUser(currentUser);
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        double total = 0;
        for (CartDetail item : cartDetails) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        CartDetailRes cartDetailRes = new CartDetailRes();
        CartDetailRes.CartRes cartRes = new CartDetailRes.CartRes();
        cartRes.setId(cart.getId());
        cartRes.setSum(cart.getSum());
        cartDetailRes.setCartRes(cartRes);
        List<CartDetailRes.CartDetailList> cartDetailList = new ArrayList<>();
        for (CartDetail item : cartDetails) {
            CartDetailRes.CartDetailList cartDetail = new CartDetailRes.CartDetailList();
            cartDetail.setId(item.getId());
            cartDetail.setPrice(item.getProduct().getPrice());
            cartDetail.setProduct(item.getProduct());
            cartDetail.setQuantity(item.getQuantity());
            cartDetailList.add(cartDetail);
        }
        cartDetailRes.setCartDetailList(cartDetailList);
        cartDetailRes.setTotal(total);
        return cartDetailRes;
    }
    
}

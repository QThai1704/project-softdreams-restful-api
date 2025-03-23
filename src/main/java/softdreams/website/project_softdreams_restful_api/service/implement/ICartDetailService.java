package softdreams.website.project_softdreams_restful_api.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.CartDetail;
import softdreams.website.project_softdreams_restful_api.domain.Product;
import softdreams.website.project_softdreams_restful_api.repository.CartDetailRepository;
import softdreams.website.project_softdreams_restful_api.service.OrderDetailService;
import softdreams.website.project_softdreams_restful_api.service.ProductService;

@Service
public class ICartDetailService implements OrderDetailService<CartDetail, Cart> {

    

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private ProductService productService;

    @Override
    public CartDetail createByOrderDetail(CartDetail cartDetail) {
        boolean checkProductExist = this.checkProductExist(cartDetail.getProduct());
        if(checkProductExist){
            CartDetail cartDetailExist = this.fetchCartDetailById(cartDetail.getProduct().getId());
            cartDetailExist.setQuantity(cartDetailExist.getQuantity() + 1);
            return this.cartDetailRepository.save(cartDetailExist);
        }
        return this.cartDetailRepository.save(cartDetail);
    }

    @Override
    public CartDetail updateByOrderDetail(Cart cart, long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateByOrderDetail'");
    }

    @Override
    public CartDetail updateByOrderDetail(Product product, long id) {
        CartDetail cartDetail = this.fetchOrderDetailById(id);
        if(this.checkProductExist(product)){
            CartDetail cartDetailExist = this.fetchCartDetailById(product.getId());
            cartDetailExist.setQuantity(cartDetail.getQuantity() + 1);
            return this.cartDetailRepository.save(cartDetailExist);
        }
        cartDetail.setProduct(product);
        return this.cartDetailRepository.save(cartDetail);
    }

    @Override
    public CartDetail updateByOrderDetailForSefl(CartDetail object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateByOrderDetailForSefl'");
    }

    @Override
    public CartDetail fetchOrderDetailById(long id) {
        CartDetail cartDetail = this.cartDetailRepository.findById(id).get();
        if(cartDetail == null) {
            throw new UnsupportedOperationException("CartDetail not found");
        }
        return cartDetail;
    }  
    
    public boolean checkProductExist(Product product) {
        return this.cartDetailRepository.existsByProduct(product);
    }

    public CartDetail fetchCartDetailById(long id) {
        return this.cartDetailRepository.findById(id).get();
    }
}

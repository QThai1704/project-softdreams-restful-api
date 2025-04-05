package softdreams.website.project_softdreams_restful_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.CartDetail;
import softdreams.website.project_softdreams_restful_api.domain.Product;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    boolean existsByProduct(Product product);
    CartDetail findByCartAndProduct(Cart cart, Product product);
    List<CartDetail> findAllCartDetailsByCartId(long cartId);
    List<CartDetail> findByProductId(long id);
}

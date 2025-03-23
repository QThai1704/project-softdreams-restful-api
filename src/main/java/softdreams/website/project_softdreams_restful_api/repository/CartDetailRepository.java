package softdreams.website.project_softdreams_restful_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import softdreams.website.project_softdreams_restful_api.domain.CartDetail;
import softdreams.website.project_softdreams_restful_api.domain.Product;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    boolean existsByProduct(Product product);
}

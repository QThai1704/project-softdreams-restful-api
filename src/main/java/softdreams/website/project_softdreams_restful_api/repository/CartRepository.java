package softdreams.website.project_softdreams_restful_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
    List<Cart> findByUserId(long id);
}

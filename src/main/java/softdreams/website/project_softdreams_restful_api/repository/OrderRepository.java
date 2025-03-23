package softdreams.website.project_softdreams_restful_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import softdreams.website.project_softdreams_restful_api.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders", nativeQuery = true)
    List<Order> findAllOrders();
    
}

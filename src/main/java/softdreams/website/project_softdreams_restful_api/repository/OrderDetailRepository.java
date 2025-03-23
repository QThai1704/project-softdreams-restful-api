package softdreams.website.project_softdreams_restful_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import softdreams.website.project_softdreams_restful_api.domain.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query(value = "SELECT od.* FROM order_details od JOIN orders o ON od.order_id = o.id WHERE o.id = :id", nativeQuery = true)
    List<OrderDetail> findAllOrderDetailsByOrderId(@Param("id") long id);
}

package softdreams.website.project_softdreams_restful_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import softdreams.website.project_softdreams_restful_api.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM products WHERE name LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<Product> filterProductByAsus(@Param("keyword") String keyword);
    Page<Product> findAll(Pageable pageable);
}

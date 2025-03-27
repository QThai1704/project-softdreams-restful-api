package softdreams.website.project_softdreams_restful_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import softdreams.website.project_softdreams_restful_api.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM products WHERE name LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<Product> filterProductByAsus(@Param("keyword") String keyword);
    Page<Product> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM products LIMIT :size OFFSET :offset", nativeQuery = true)
    List<Product> getAllProductsNativeQuery(@Param("size") long size, @Param("offset") long offset);

    // Câu lệnh Update tất cả các thuộc tính
    @Query(value = "UPDATE products SET name = :name, price = :price, quantity = :quantity, detail_desc = :detail_desc, image = :image, short_desc = :short_desc WHERE id = :id", nativeQuery = true)
    Product updateProductNativeQuery(
        @Param("id") long id, 
        @Param("name") String name, 
        @Param("price") double price, 
        @Param("quantity") long quantity, 
        @Param("detail_desc") String detail_desc, 
        @Param("image") String image, 
        @Param("short_desc") String short_desc);

    @Modifying
    @Query(value = "DELETE FROM products WHERE id = :id", nativeQuery = true)
    void deleteProductNativeQuery(@Param("id") long id);
}

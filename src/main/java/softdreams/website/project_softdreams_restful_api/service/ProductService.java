package softdreams.website.project_softdreams_restful_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import softdreams.website.project_softdreams_restful_api.domain.Product;
import softdreams.website.project_softdreams_restful_api.dto.request.ProductReq;
import softdreams.website.project_softdreams_restful_api.dto.response.CartDetailRes.CartDetailList;
import softdreams.website.project_softdreams_restful_api.dto.response.ProductRes;
import softdreams.website.project_softdreams_restful_api.dto.response.ResPagination;

public interface ProductService {
    Product createProduct(ProductReq productReq);
    ProductRes ResProductCreate(ProductReq productReq);
    List<Product> fetchAllProduct();
    List<ProductRes> ResProductFetchAllProduct(List<Product> products);
    Optional<Product> fetchProductById(long id);
    ProductRes ResProductFetchProduct(long id);
    Product updateProduct(ProductReq productReq);
    ProductRes ResProductUpdate(ProductReq productReq);
    void deleteProduct(long id);
    List<Product> filterProductByNameAsus(String keyword);
    Page<Product> fetchAllProductPage(Pageable pageable);
    void deleteProductNativeQuery(long id);
    Product updateProductNativeQuery(ProductReq productReq);
    List<Product> getAllProductsNativeQuery(long size, long offset);
    ResPagination fetchAllProductPageRes(Page<Product> prs, Pageable pageable);
    CartDetailList handleAddProductToCart(String email, long productId, int sum, long quantity);
}

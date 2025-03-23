package softdreams.website.project_softdreams_restful_api.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import softdreams.website.project_softdreams_restful_api.domain.Product;
import softdreams.website.project_softdreams_restful_api.dto.request.ProductReq;
import softdreams.website.project_softdreams_restful_api.dto.response.ProductRes;
import softdreams.website.project_softdreams_restful_api.repository.ProductRepository;
import softdreams.website.project_softdreams_restful_api.service.ProductService;

@Service
public class IProductService implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(ProductReq productReq) {
        Product newProduct = new Product();
        newProduct.setName(productReq.getName());
        newProduct.setPrice(productReq.getPrice());
        newProduct.setImage(productReq.getImage());
        newProduct.setDetailDesc(productReq.getDetailDesc());
        newProduct.setShortDesc(productReq.getShortDesc());
        newProduct.setQuantity(productReq.getQuantity());
        return this.productRepository.save(newProduct);
    }

    @Override
    public ProductRes ResProductCreate(ProductReq productReq) {
        Product product = this.createProduct(productReq);
        ProductRes productRes = new ProductRes();
        productRes.setId(product.getId());
        productRes.setName(product.getName());
        productRes.setPrice(product.getPrice());
        productRes.setImage(product.getImage());
        productRes.setDetailDesc(product.getDetailDesc());
        productRes.setShortDesc(product.getShortDesc());
        productRes.setQuantity(product.getQuantity());
        return productRes;
    }

    @Override
    public List<Product> fetchAllProduct() {
        return this.productRepository.findAll();
    }

    public List<ProductRes> ResProductFetchAllProduct(List<Product> products) {
        List<ProductRes> productRes = products.stream().map(product -> {
            ProductRes productResItem = new ProductRes();
            productResItem.setId(product.getId());
            productResItem.setName(product.getName());
            productResItem.setPrice(product.getPrice());
            productResItem.setImage(product.getImage());
            productResItem.setDetailDesc(product.getDetailDesc());
            productResItem.setShortDesc(product.getShortDesc());
            productResItem.setQuantity(product.getQuantity());
            return productResItem;
        }).toList();
        return productRes;
    };

    @Override
    public Optional<Product> fetchProductById(long id) {
        return this.productRepository.findById(id);
    }

    @Override
    public ProductRes ResProductFetchProduct(long id) {
        Product product = this.fetchProductById(id).get();
        if(product == null) {
            throw new UnsupportedOperationException("Sản phẩm không tồn tại");
        }
        ProductRes productRes = new ProductRes();
        productRes.setId(product.getId());
        productRes.setName(product.getName());
        productRes.setPrice(product.getPrice());
        productRes.setImage(product.getImage());
        productRes.setDetailDesc(product.getDetailDesc());
        productRes.setShortDesc(product.getShortDesc());
        productRes.setQuantity(product.getQuantity());
        return productRes;
    }

    @Override
    public Product updateProduct(ProductReq productReq) {
        Product currentProduct = this.fetchProductById(productReq.getId()).get();
        if(currentProduct == null) {
            throw new UnsupportedOperationException("Sản phẩm không tồn tại");
        }
        currentProduct.setName(productReq.getName());
        currentProduct.setPrice(productReq.getPrice());
        currentProduct.setImage(productReq.getImage());
        currentProduct.setDetailDesc(productReq.getDetailDesc());
        currentProduct.setShortDesc(productReq.getShortDesc());
        currentProduct.setQuantity(productReq.getQuantity());
        return this.productRepository.save(currentProduct);
    }

    @Override
    public ProductRes ResProductUpdate(ProductReq productReq) {
        Product product = this.updateProduct(productReq);
        ProductRes productRes = new ProductRes();
        productRes.setId(product.getId());
        productRes.setName(product.getName());
        productRes.setPrice(product.getPrice());
        productRes.setImage(product.getImage());
        productRes.setDetailDesc(product.getDetailDesc());
        productRes.setShortDesc(product.getShortDesc());
        productRes.setQuantity(product.getQuantity());
        return productRes;
    }

    @Override
    public void deleteProduct(long id) {
        this.productRepository.deleteById(id);
    }

    @Override
    public List<Product> filterProductByNameAsus(String keyword) {
        return this.productRepository.filterProductByAsus(keyword);
    }
    
}

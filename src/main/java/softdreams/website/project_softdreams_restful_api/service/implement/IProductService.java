package softdreams.website.project_softdreams_restful_api.service.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.CartDetail;
import softdreams.website.project_softdreams_restful_api.domain.Product;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.ProductReq;
import softdreams.website.project_softdreams_restful_api.dto.response.ProductRes;
import softdreams.website.project_softdreams_restful_api.dto.response.ResPagination;
import softdreams.website.project_softdreams_restful_api.repository.CartDetailRepository;
import softdreams.website.project_softdreams_restful_api.repository.CartRepository;
import softdreams.website.project_softdreams_restful_api.repository.ProductRepository;
import softdreams.website.project_softdreams_restful_api.repository.UserRepository;
import softdreams.website.project_softdreams_restful_api.service.ProductService;
import softdreams.website.project_softdreams_restful_api.service.UserService;

@Service
public class IProductService implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Override
    public Product createProduct(ProductReq productReq) {
        String imageStr = this.handleStringImage(productReq.getImage());
        Product newProduct = new Product();
        newProduct.setName(productReq.getName());
        newProduct.setPrice(productReq.getPrice());
        newProduct.setImage(imageStr);
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
        String imageStr = this.handleStringImage(productReq.getImage());
        Product currentProduct = this.fetchProductById(productReq.getId()).get();
        if(currentProduct == null) {
            throw new UnsupportedOperationException("Sản phẩm không tồn tại");
        }
        currentProduct.setName(productReq.getName());
        currentProduct.setPrice(productReq.getPrice());
        currentProduct.setImage(imageStr);
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

    @Override
    public Page<Product> fetchAllProductPage(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    @Override
    public ResPagination fetchAllProductPageRes(Page<Product> prs, Pageable pageable) {
        ResPagination resPagination = new ResPagination();
        resPagination.setMeta(ResPagination.addMeta(prs, pageable));
        resPagination.setData(this.ResProductFetchAllProduct(prs.getContent()));
        return resPagination;
    }

    @Override
    @Transactional
    public void deleteProductNativeQuery(long id) {
        this.productRepository.deleteProductNativeQuery(id);
    }

    @Override
    public Product updateProductNativeQuery(ProductReq productReq) {
        return this.productRepository.updateProductNativeQuery(productReq.getId(), productReq.getName(), productReq.getPrice(), productReq.getQuantity(), productReq.getDetailDesc(), productReq.getImage(), productReq.getShortDesc());
    }

    @Override
    public List<Product> getAllProductsNativeQuery(long size, long page) {
        long offset = (page - 1) * size;
        return this.productRepository.getAllProductsNativeQuery(size, offset);
    }
    
    public String handleStringImage(String image) {
        if(image != null && image.contains("C:\\fakepath\\")){
            return image.replace("C:\\fakepath\\", "");
        }
        return image;
    }

    public ProductRes convertProductToResProduct(Product product){
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
    public void handleAddProductToCart(String email, long productId, int sum, long quantity) {
        User user = this.userRepository.findByEmail(email).get();
        if(user != null){
            Cart cart = this.cartRepository.findByUser(user);
            if(cart == null){
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setSum(0);
                cart = this.cartRepository.save(newCart);
            }
            Product product = this.productRepository.findById(productId).get();
            CartDetail oldCartDetail =  this.cartDetailRepository.findByCartAndProduct(cart, product);
            if(oldCartDetail == null){
                CartDetail cartDetail = new CartDetail();
                cartDetail.setCart(cart);
                cartDetail.setProduct(product);
                cartDetail.setQuantity(1);
                cartDetail.setPrice(product.getPrice());
                this.cartDetailRepository.save(cartDetail);
                int sumProduct = cart.getSum() + 1;
                cart.setSum(sum);
                this.cartRepository.save(cart);
                sum = sumProduct;
            }else {
                oldCartDetail.setQuantity(oldCartDetail.getQuantity() + 1);
                this.cartDetailRepository.save(oldCartDetail);
            }
        }
    }
}

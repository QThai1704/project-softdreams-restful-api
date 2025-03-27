package softdreams.website.project_softdreams_restful_api.controller;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import softdreams.website.project_softdreams_restful_api.domain.Product;
import softdreams.website.project_softdreams_restful_api.dto.request.ProductReq;
import softdreams.website.project_softdreams_restful_api.dto.response.ProductRes;
import softdreams.website.project_softdreams_restful_api.dto.response.ResGlobal;
import softdreams.website.project_softdreams_restful_api.dto.response.ResPagination;
import softdreams.website.project_softdreams_restful_api.service.ProductService;
import softdreams.website.project_softdreams_restful_api.util.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping({"/admin/product"})
    @ApiMessage(message = "Thêm sản phẩm")
    public ResponseEntity<ProductRes> createProduct(@Valid @RequestBody ProductReq productReq) {
        ProductRes productRes = this.productService.ResProductCreate(productReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRes);
    }

    // @GetMapping("/product")
    // @ApiMessage(message = "Danh sách sản phẩm")
    // public ResponseEntity<List<ProductRes>> fetchAllProductApi() {
    //     List<Product> products = this.productService.fetchAllProduct();
    //     List<ProductRes> productList = this.productService.ResProductFetchAllProduct(products);
    //     return ResponseEntity.status(HttpStatus.OK).body(productList);
    // }

    @GetMapping({"/product/{id}", "/admin/product/{id}"})
    @ApiMessage(message = "Tìm kiếm sản phẩm")
    public ResponseEntity<ProductRes> fetchProductById(@PathVariable("id") long id) {
        ProductRes product = this.productService.ResProductFetchProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PutMapping({"/admin/product"})
    @ApiMessage(message = "Cập nhật sản phẩm")
    public ResponseEntity<ProductRes> updateProduct(@Valid @RequestBody ProductReq productReq) {
        ProductRes productRes = this.productService.ResProductUpdate(productReq);
        return ResponseEntity.status(HttpStatus.OK).body(productRes);
    }

    @DeleteMapping({"/admin/product/{id}"})
    @ApiMessage(message = "Xóa sản phẩm")
    public ResponseEntity<ResGlobal> deleteProduct(@PathVariable("id") long id) {
        ResGlobal<Object> response = new ResGlobal<>();
        this.productService.deleteProduct(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping({"/filterProduct/{keyword}"})
    @ApiMessage(message = "Danh sách sản phẩm Asus")
    public ResponseEntity<List<ProductRes>> fetchAllProductAsusApi(@PathVariable("keyword") String keyword) {
        List<Product> products = this.productService.filterProductByNameAsus(keyword);
        List<ProductRes> productList = this.productService.ResProductFetchAllProduct(products);
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping({"/product", "/admin/product"})
    @ApiMessage(message = "Danh sách sản phẩm phân trang")
    public ResponseEntity<ResPagination> fetchAllProductPage(
        @RequestParam("page") Optional<String> pageOptional,
        @RequestParam("size") Optional<String> sizeOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                page = Integer.parseInt(pageOptional.get());
            }
        } catch (Exception e) {
            page = 1;
        }
        Pageable pageable = PageRequest.of((page - 1), Integer.parseInt(sizeOptional.get()));
        Page<Product> prs = this.productService.fetchAllProductPage(pageable);
        ResPagination resPagination = this.productService.fetchAllProductPageRes(prs, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resPagination);
    }

    @DeleteMapping({"/admin/productNativeQuery/{id}"})
    @ApiMessage(message = "Xóa sản phẩm sử dụng Native Query")
    public ResponseEntity<Void> deleteProductNativeQuery(@PathVariable("id") long id) {
        this.productService.deleteProductNativeQuery(id);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping({"/admin/productNativeQuery"})
    @ApiMessage(message = "Cập nhật sản phẩm sử dụng Native Query")
    public ResponseEntity<ProductRes> updateProductNativeQuery(@Valid @RequestBody ProductReq productReq) {
        ProductRes productRes = this.productService.ResProductUpdate(productReq);
        return ResponseEntity.ok().body(productRes);
    }

    @GetMapping({"/productNativeQuery", "/admin/productNativeQuery"})
    @ApiMessage(message = "Danh sách sản phẩm sử dụng Native Query")
    public ResponseEntity<List<ProductRes>> getAllProductsNativeQuery(
        @RequestParam("page") Optional<String> pageOptional,
        @RequestParam("size") Optional<String> sizeOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                page = Integer.parseInt(pageOptional.get());
            }
        } catch (Exception e) {
            page = 1;
        }
        List<Product> products = this.productService.getAllProductsNativeQuery(Integer.parseInt(sizeOptional.get()), page);
        List<ProductRes> productResList = this.productService.ResProductFetchAllProduct(products);
        return ResponseEntity.ok().body(productResList);
    }

    @Value("${upload-file.base-uri}")
    String IMAGE_DIR;
    @GetMapping({"/image/{filename}"})
    @ApiMessage(message = "Lấy ảnh sản phẩm")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        File file = new File(IMAGE_DIR+ "system" + filename);
        if (!file.exists() || file.isDirectory()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Hoặc IMAGE_PNG tùy định dạng
                .body(resource);
    }
        
}

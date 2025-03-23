package softdreams.website.project_softdreams_restful_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import softdreams.website.project_softdreams_restful_api.domain.Product;
import softdreams.website.project_softdreams_restful_api.dto.request.ProductReq;
import softdreams.website.project_softdreams_restful_api.dto.response.ProductRes;
import softdreams.website.project_softdreams_restful_api.dto.response.ResGlobal;
import softdreams.website.project_softdreams_restful_api.service.ProductService;
import softdreams.website.project_softdreams_restful_api.util.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    @ApiMessage(message = "Thêm sản phẩm")
    public ResponseEntity<ProductRes> createProduct(@RequestBody ProductReq productReq) {
        ProductRes productRes = this.productService.ResProductCreate(productReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRes);
    }

    @GetMapping("/product")
    @ApiMessage(message = "Danh sách sản phẩm")
    public ResponseEntity<List<ProductRes>> fetchAllProductApi() {
        List<Product> products = this.productService.fetchAllProduct();
        List<ProductRes> productList = this.productService.ResProductFetchAllProduct(products);
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping("/product/{id}")
    @ApiMessage(message = "Tìm kiếm sản phẩm")
    public ResponseEntity<ProductRes> fetchProductById(@PathVariable("id") long id) {
        ProductRes product = this.productService.ResProductFetchProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PutMapping("/product")
    @ApiMessage(message = "Cập nhật sản phẩm")
    public ResponseEntity<ProductRes> updateProduct(@RequestBody ProductReq productReq) {
        ProductRes productRes = this.productService.ResProductUpdate(productReq);
        return ResponseEntity.status(HttpStatus.OK).body(productRes);
    }

    @DeleteMapping("/product/{id}")
    @ApiMessage(message = "Xóa sản phẩm")
    public ResponseEntity<ResGlobal> deleteProduct(@PathVariable("id") long id) {
        ResGlobal<Object> response = new ResGlobal<>();
        this.productService.deleteProduct(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/filterProduct/{keyword}")
    @ApiMessage(message = "Danh sách sản phẩm Asus")
    public ResponseEntity<List<ProductRes>> fetchAllProductAsusApi(@PathVariable("keyword") String keyword) {
        List<Product> products = this.productService.filterProductByNameAsus(keyword);
        List<ProductRes> productList = this.productService.ResProductFetchAllProduct(products);
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }
}

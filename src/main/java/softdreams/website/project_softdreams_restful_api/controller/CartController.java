package softdreams.website.project_softdreams_restful_api.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.CartDetail;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.CartReq;
import softdreams.website.project_softdreams_restful_api.dto.request.ReceiverReq;
import softdreams.website.project_softdreams_restful_api.dto.response.CartDetailRes;
import softdreams.website.project_softdreams_restful_api.dto.response.CartDetailRes.CartDetailList;
import softdreams.website.project_softdreams_restful_api.exception.CustomException;
import softdreams.website.project_softdreams_restful_api.service.CartService;
import softdreams.website.project_softdreams_restful_api.service.OrderDetailService;
import softdreams.website.project_softdreams_restful_api.service.ProductService;
import softdreams.website.project_softdreams_restful_api.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @PostMapping("/add-product-to-cart")
    public ResponseEntity<CartDetailList> addProductToCart(@RequestBody() CartReq cartReq) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CartDetailList CartDetailItem = this.productService.handleAddProductToCart(email, cartReq.getProductId(), cartReq.getSum(), cartReq.getQuantity());
        return ResponseEntity.ok().body(CartDetailItem);
    }

    @GetMapping({"/cart", "/checkout"})
    public ResponseEntity<CartDetailRes> getCartPage() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CartDetailRes cartDetailRes = this.cartService.convertCartDetailRes(email);
        return ResponseEntity.ok().body(cartDetailRes);
    }

    @PostMapping("/confirm-checkout")
    public ResponseEntity<String> getCheckOutPage(@RequestBody() List<CartDetailRes.CartDetailList> cartDetailList) throws CustomException {
        if(cartDetailList.size() == 0) {
            throw new CustomException("Giỏ hàng trống, hãy lựa chọn sản phẩm trước khi thanh toán");
        }
        this.cartService.handleUpdateCartBeforeCheckout(cartDetailList);
        return ResponseEntity.ok().body("Kiểm tra giỏ hàng thành công"); 
    }

    // Đặt hàng
    @PostMapping("/place-order")
    public ResponseEntity<String> handlePlaceOrderApi(
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverAddress") String receiverAddress,
            @RequestParam("receiverPhone") String receiverPhone,
            @RequestParam("totalPrice") String totalPrice) throws NumberFormatException, UnsupportedEncodingException {
        User currentUser = new User();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        currentUser = this.userService.findUserByEmail(email).get();
        ReceiverReq newReceiverReq = ReceiverReq.builder()
                .receiverName(receiverName)
                .receiverAddress(receiverAddress)
                .receiverPhone(receiverPhone)
                .build();
        final String uuid = UUID.randomUUID().toString().replace("", "");
        this.cartService.handlePlaceOrder(currentUser, newReceiverReq, 5, uuid);
        return ResponseEntity.ok().body("Đặt hàng thành công");
    }

    @PostMapping("/delete-cart-product")
    public ResponseEntity<String> postDeleteCartProduct(@RequestParam("id") long id) {
        this.cartService.deleteCartProduct(id);
        return ResponseEntity.ok()
        .contentType(MediaType.TEXT_PLAIN) 
        .body("Xóa sản phẩm khỏi giỏ hàng thành công");
    }
}

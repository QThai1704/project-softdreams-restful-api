package softdreams.website.project_softdreams_restful_api.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private OrderDetailService<CartDetail, Cart> orderDetailService;

    @PostMapping("/add-product-to-cart")
    public ResponseEntity<Integer> addProductToCart(@RequestBody() CartReq cartReq) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Email: " + email);

        this.productService.handleAddProductToCart(email, cartReq.getProductId(), cartReq.getSum(), cartReq.getQuantity());

        log.info("Tổng số lượng sản phẩm có trong giỏ hàng: " + cartReq.getSum());
        log.info("Add product to cart successfully");
        return ResponseEntity.ok().body(cartReq.getSum());
    }

    @GetMapping({"/cart", "/checkout"})
    public ResponseEntity<CartDetailRes> getCartPage() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CartDetailRes cartDetailRes = this.cartService.convertCartDetailRes(email);
        return ResponseEntity.ok().body(cartDetailRes);
    }

    // // Kiêm tra lại hàm
    // @GetMapping("/checkout")
    // public String getCheckOutPage(HttpServletRequest request) {
    //     String email = SecurityContextHolder.getContext().getAuthentication().getName();
    //     User currentUser = this.userService.findUserByEmail(email).get();
    //     Cart cart = this.cartService.fetchByUser(currentUser);

    //     List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

    //     double totalPrice = 0;
    //     for (CartDetail cd : cartDetails) {
    //         totalPrice += cd.getPrice() * cd.getQuantity();
    //     }

    //     model.addAttribute("cartDetails", cartDetails);
    //     model.addAttribute("totalPrice", totalPrice);

    //     return "client/cart/checkout";
    // }

    @PostMapping("/confirm-checkout")
    public ResponseEntity<String> getCheckOutPage(@RequestBody() List<CartDetailRes.CartDetailList> cartDetailList) {
        // List<CartDetailRes.CartDetailList> cartDetailList = cartDetailRes == null ? new ArrayList<CartDetailRes.CartDetailList>() : cartDetailRes.getCartDetailList();
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

    // @GetMapping("/thanks")
    // public String getThankYouPage() {
    //     if (vnpayResponseCode.isPresent() && paymentRef.isPresent()) {
    //         // thanh toán qua VNPAY, cập nhật trạng thái order
    //         PaymentStatusEnum paymentStatus = vnpayResponseCode.get().equals("00")
    //                 ? PaymentStatusEnum.PAYMENT_SUCCEED
    //                 :  PaymentStatusEnum.PAYMENT_FAILED;
    //         this.productService.updatePaymentStatus(paymentRef.get(), paymentStatus);
    //     }
    //     return "client/cart/thanks";
    // }

    @PostMapping("/delete-cart-product")
    public ResponseEntity<String> postDeleteCartProduct(@RequestParam("id") long id) {
        this.cartService.deleteCartProduct(id);
        return ResponseEntity.ok()
        .contentType(MediaType.TEXT_PLAIN) 
        .body("Xóa sản phẩm khỏi giỏ hàng thành công");
    }
}

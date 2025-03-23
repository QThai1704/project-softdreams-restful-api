package softdreams.website.project_softdreams_restful_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.CartDetail;
import softdreams.website.project_softdreams_restful_api.dto.response.ResGlobal;
import softdreams.website.project_softdreams_restful_api.service.CartService;
import softdreams.website.project_softdreams_restful_api.service.OrderDetailService;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderDetailService<CartDetail, Cart> orderDetailService;

    @PostMapping("/add-product")
    @Transactional
    public ResponseEntity<String> createCartDetail(@RequestBody CartDetail cartDetail, @RequestParam("email") String email) {
        CartDetail newCartDetail = this.orderDetailService.createByOrderDetail(cartDetail);
        Cart cart = this.cartService.fetchCartByUser(email);
        if (cart == null) {
            cart = this.cartService.initOrder(email);
            newCartDetail.setCart(cart);
        }
        else {
            newCartDetail.setCart(cart);
        }
        return ResponseEntity.ok().body("Product added to cart");
    }
}

package softdreams.website.project_softdreams_restful_api.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.CartDetail;
import softdreams.website.project_softdreams_restful_api.domain.Order;
import softdreams.website.project_softdreams_restful_api.domain.OrderDetail;
import softdreams.website.project_softdreams_restful_api.domain.Product;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.ReceiverReq;
import softdreams.website.project_softdreams_restful_api.dto.response.CartDetailRes;
import softdreams.website.project_softdreams_restful_api.repository.CartDetailRepository;
import softdreams.website.project_softdreams_restful_api.repository.CartRepository;
import softdreams.website.project_softdreams_restful_api.repository.OrderDetailRepository;
import softdreams.website.project_softdreams_restful_api.repository.OrderRepository;
import softdreams.website.project_softdreams_restful_api.repository.UserRepository;
import softdreams.website.project_softdreams_restful_api.service.CartService;
import softdreams.website.project_softdreams_restful_api.util.constants.StatusEnum;

@Service
public class ICartService implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;


    @Override
    public Cart fetchByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    // Thêm sản phẩm và chi tiết sản phẩm vào giỏ hàng.
    // Chưa xử lý sum.
    @Override
    public CartDetailRes convertCartDetailRes(String email) {
        User currentUser = this.userRepository.findByEmail(email).get();
        Cart cart = this.cartRepository.findByUser(currentUser);
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        double total = 0;
        for (CartDetail item : cartDetails) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        CartDetailRes cartDetailRes = new CartDetailRes();
        CartDetailRes.CartRes cartRes = new CartDetailRes.CartRes();
        cartRes.setId(cart.getId());
        cartRes.setSum(cart.getSum());
        cartDetailRes.setCartRes(cartRes);
        List<CartDetailRes.CartDetailList> cartDetailList = new ArrayList<>();
        for (CartDetail item : cartDetails) {
            CartDetailRes.CartDetailList cartDetail = new CartDetailRes.CartDetailList();
            cartDetail.setId(item.getId());
            cartDetail.setPrice(item.getProduct().getPrice());
            cartDetail.setProduct(item.getProduct());
            cartDetail.setQuantity(item.getQuantity());
            cartDetailList.add(cartDetail);
        }
        cartDetailRes.setCartDetailList(cartDetailList);
        cartDetailRes.setTotal(total);
        return cartDetailRes;
    }

    // Kiểm tra lại các sản phẩm trước khi thanh toán
    // 1. Nếu số lượng sản phẩm thay đổi thì cập nhật lại.
    @Override
    public void handleUpdateCartBeforeCheckout(List<CartDetailRes.CartDetailList> cartDetailList) {
        for (CartDetailRes.CartDetailList cartDetail : cartDetailList) {
            Optional<CartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());
            if (cdOptional.isPresent()) {
                CartDetail currentCartDetail = cdOptional.get();
                currentCartDetail.setQuantity(cartDetail.getQuantity());
                this.cartDetailRepository.save(currentCartDetail);
            }
        }
    }

    // Thanh toán sản phẩm
    @Override
    @Transactional
    public void handlePlaceOrder(User user, ReceiverReq receiverReq, int sum, String uuid) {
        double totalPrice = 0;
        Order order = new Order();
        order.setUser(user);
        order.setOrderCode("HD" + uuid);
        order.setReceiverName(receiverReq.getReceiverName());
        order.setReceiverAddress(receiverReq.getReceiverAddress());
        order.setReceiverPhone(receiverReq.getReceiverPhone());
        order.setStatus(StatusEnum.PENDING);
        order = this.orderRepository.save(order);

        // Tạo đơn hàng chi tiết
        // Lấy giỏ hàng của user
        Cart cart = this.cartRepository.findByUser(user);
        if (cart != null) {
            List<CartDetail> cartDetails = cart.getCartDetails();
            for (CartDetail cartDetail : cartDetails) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(cartDetail.getProduct());
                orderDetail.setQuantity(cartDetail.getQuantity());
                orderDetail.setPrice(cartDetail.getPrice());
                this.orderDetailRepository.save(orderDetail);
            }

            // Tính tổng tiền của đơn hàng
            // Xóa đi các bản ghi trong bảng chi tiết giỏ hàng
            for (CartDetail cartDetail : cartDetails) {
                totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
                this.cartDetailRepository.delete(cartDetail);
            }

            
            order.setTotalPrice(totalPrice);
            this.orderRepository.save(order);
            // Xóa giỏ hàng
            cart.setSum(0);
            this.cartRepository.save(cart);

            // Cập nhật lại số lượng sản phẩm
            sum = 0 ;
        }
    }

    @Override
    public void deleteCartProduct(long id) {
        Optional<CartDetail> cartOptional = this.cartDetailRepository.findById(id);
        if (cartOptional.isPresent()) {
            CartDetail cartDetail = cartOptional.get();
            Cart cart = cartDetail.getCart();
            this.cartDetailRepository.deleteById(id);
            if (cart.getSum() > 1) {
                int sum = cart.getSum() - 1;
                cart.setSum(sum);
                this.cartRepository.save(cart);
            } else {
                this.cartRepository.delete(cart);
            }
        }
    }
}

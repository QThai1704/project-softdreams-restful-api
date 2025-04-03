package softdreams.website.project_softdreams_restful_api.service.implement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import softdreams.website.project_softdreams_restful_api.domain.Order;
import softdreams.website.project_softdreams_restful_api.domain.OrderDetail;
import softdreams.website.project_softdreams_restful_api.domain.Product;
import softdreams.website.project_softdreams_restful_api.dto.response.OrderRes;
import softdreams.website.project_softdreams_restful_api.repository.OrderDetailRepository;
import softdreams.website.project_softdreams_restful_api.repository.OrderRepository;
import softdreams.website.project_softdreams_restful_api.repository.ProductRepository;
import softdreams.website.project_softdreams_restful_api.service.OrderService;


@Service
public class IOrderService implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Order createOrder(Order order) {
       return this.orderRepository.save(order);
    }

    @Override
    public OrderRes resCreateOrder(Order order) {
        OrderRes orderRes = this.convertOrderToOrderRes(order);
        return orderRes;
    }

    @Override
    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    @Override
    public List<OrderRes> resAllOrders(List<Order> orders) {
        List<OrderRes> orderResList = orders.stream().map(order -> {
            OrderRes orderResItem = this.convertOrderToOrderRes(order);
            return orderResItem;
        }).collect(Collectors.toList());
        return orderResList;
    }

    @Override
    public Order fetchOrderById(long id) {
        Optional<Order> orderById = this.orderRepository.findById(id);
        if (orderById.isPresent()) {
            return orderById.get();
        }
        return null;
    }

    @Override
    public OrderRes resFetchOrderById(Order order) {
        OrderRes orderRes = this.convertOrderToOrderRes(order);
        return orderRes;
    }

    @Override
    public Order updateOrder(Order order) {
        Order current = this.fetchOrderById(order.getId());
        if (current != null) {
            current.setId(order.getId());
            current.setReceiverName(current.getReceiverName());
            current.setReceiverAddress(current.getReceiverAddress());
            current.setReceiverPhone(current.getReceiverPhone());
            current.setTotalPrice(current.getTotalPrice());
            current.setStatus(order.getStatus());
            return this.orderRepository.save(current);
        }
        return null;
    }

    @Override
    public OrderRes resUpdateOrder(Order order) {
        OrderRes orderRes = this.convertOrderToOrderRes(order);
        return orderRes;
    }

    @Override
    public ResponseEntity<Void> deleteOrder(long id) {
        Optional<Order> orderById = this.orderRepository.findById(id);
        if (!orderById.isPresent()) {
            System.out.println("Order not found");
            return ResponseEntity.notFound().build();
        }
        for (OrderDetail orderDetail : orderById.get().getOrderDetails()) {
            this.orderDetailRepository.delete(orderDetail);
        }
        this.orderRepository.deleteById(id);
        return ResponseEntity.ok().body(null);
    }

    @Override
    public OrderRes convertOrderToOrderRes(Order order) {
        OrderRes orderRes = new OrderRes();
        OrderRes.User_OrderRes user = new OrderRes.User_OrderRes();
        List<OrderDetail> orderDetails = this.orderDetailRepository.findAllOrderDetailsByOrderId(order.getId());
        orderRes.setId(order.getId());
        orderRes.setOrderCode(order.getOrderCode());
        orderRes.setTotalPrice(order.getTotalPrice());
        orderRes.setReceiverName(order.getReceiverName());
        orderRes.setReceiverAddress(order.getReceiverAddress());
        orderRes.setReceiverPhone(order.getReceiverPhone());
        orderRes.setStatus(order.getStatus());
        user.setFullName(order.getUser().getFullName());
        user.setEmail(order.getUser().getEmail());
        orderRes.setUser(user);
        orderRes.setOrderDetails(
            // Xử lý OrderDetail
            orderDetails.stream().map(orderDetailItem -> {
            OrderRes.OrderDetail_OrderRes orderDetail = new OrderRes.OrderDetail_OrderRes();
            orderDetail.setId(orderDetailItem.getId());
            orderDetail.setQuantity(orderDetailItem.getQuantity());
            orderDetail.setPrice(orderDetailItem.getPrice());

            OrderRes.OrderDetail_OrderRes.Product_OrderRes product = new OrderRes.OrderDetail_OrderRes.Product_OrderRes();
            product.setName(orderDetailItem.getNameProduct());
            product.setPrice(orderDetailItem.getPriceProduct());
            product.setImage(orderDetailItem.getImgProduct());
            orderDetail.setProduct(product);
            return orderDetail;
        }).collect(Collectors.toList()));
        return orderRes;
    }

    @Override
    public List<Order> findByOrderCode(String orderCode) {
        List<Order> order = this.orderRepository.findByOrderCode(orderCode);
        return order;
    }
}

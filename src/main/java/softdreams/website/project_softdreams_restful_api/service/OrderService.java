package softdreams.website.project_softdreams_restful_api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import softdreams.website.project_softdreams_restful_api.domain.Order;
import softdreams.website.project_softdreams_restful_api.dto.response.OrderRes;

public interface OrderService {
    Order createOrder(Order order);

    OrderRes resCreateOrder(Order order);

    List<Order> getAllOrders();

    List<OrderRes> resAllOrders(List<Order> orders);

    Order fetchOrderById(long id);

    OrderRes resFetchOrderById(Order order);

    Order updateOrder(Order order);

    OrderRes resUpdateOrder(Order order);

    ResponseEntity<Void> deleteOrder(long id);

}

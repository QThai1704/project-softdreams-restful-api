package softdreams.website.project_softdreams_restful_api.controller;

import java.util.List;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.Order;
import softdreams.website.project_softdreams_restful_api.domain.OrderDetail;
import softdreams.website.project_softdreams_restful_api.dto.response.OrderRes;
import softdreams.website.project_softdreams_restful_api.dto.response.ResGlobal;
import softdreams.website.project_softdreams_restful_api.service.OrderDetailService;
import softdreams.website.project_softdreams_restful_api.service.OrderService;
import softdreams.website.project_softdreams_restful_api.service.implement.IOrderDetailService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;
    
    @PostMapping({"/admin/order"})
    public ResponseEntity<OrderRes> createOrderApi(@RequestBody Order order) {
        Order newOrder = this.orderService.createOrder(order);
        OrderRes orderRes = this.orderService.resCreateOrder(newOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderRes);
    }

    @GetMapping({"/order/{id}", "/admin/order/{id}"})
    public ResponseEntity<OrderRes> getOrderByIdApi(@PathVariable("id") long id) {
        Order order = this.orderService.fetchOrderById(id);
        OrderRes orderRes = this.orderService.resFetchOrderById(order);
        return ResponseEntity.ok().body(orderRes);
    }
    
    @GetMapping({"/order", "/admin/order"})
    public ResponseEntity<List<OrderRes>> getAllOrderApi() {
        List<Order> orders = this.orderService.getAllOrders();
        List<OrderRes> orderResList = this.orderService.resAllOrders(orders);
        return ResponseEntity.ok().body(orderResList);
    }

    @PutMapping({"/order", "/admin/order"})
    public ResponseEntity<OrderRes> updateOrderApi(@RequestBody Order order) {
        Order updateOrder = this.orderService.updateOrder(order);
        OrderRes orderRes = this.orderService.resUpdateOrder(updateOrder);
        return ResponseEntity.ok().body(orderRes);
    }

    @DeleteMapping("/admin/order/{id}")
    public ResponseEntity<String> deleteOrderApi(@PathVariable("id") long id) {
        // Bug: Can kiem tra order co id co ton tai hay khong roi moi xoa
        this.orderService.deleteOrder(id);
        return ResponseEntity
                .ok()
                .body("Xoa thanh cong");
    }

    @GetMapping({"admin/order-detail/{id-order}"})
    public ResponseEntity<List<OrderDetail>> getOrderDetailByOrderIdApi(@PathVariable("id-order") long id) {
        List<OrderDetail> orderDetails = this.orderDetailService.fetchOrderDetailByOrderId(id);
        return ResponseEntity.ok().body(orderDetails);
    }

    
}

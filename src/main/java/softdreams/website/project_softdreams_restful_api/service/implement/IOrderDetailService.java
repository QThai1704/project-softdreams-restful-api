package softdreams.website.project_softdreams_restful_api.service.implement;

import java.util.List;
import java.util.stream.Collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import softdreams.website.project_softdreams_restful_api.domain.Order;
import softdreams.website.project_softdreams_restful_api.domain.OrderDetail;
import softdreams.website.project_softdreams_restful_api.domain.Product;
import softdreams.website.project_softdreams_restful_api.repository.OrderDetailRepository;
import softdreams.website.project_softdreams_restful_api.service.OrderDetailService;

@Service
public class IOrderDetailService implements OrderDetailService<OrderDetail, Order>{

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetail createByOrderDetail(OrderDetail orderDetail) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createByOrderDetail'");
    }

    @Override
    public OrderDetail updateByOrderDetail(Order object, long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateByOrderDetail'");
    }

    @Override
    public OrderDetail updateByOrderDetail(Product product, long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateByOrderDetail'");
    }

    @Override
    public OrderDetail updateByOrderDetailForSefl(OrderDetail object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateByOrderDetailForSefl'");
    }

    @Override
    public OrderDetail fetchOrderDetailById(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fetchOrderDetailById'");
    }

    public List<OrderDetail> fetchOrderDetailByOrderId(long id) {
        return this.orderDetailRepository.findAllOrderDetailsByOrderId(id);
    }
}

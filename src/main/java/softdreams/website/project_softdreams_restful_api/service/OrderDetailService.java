package softdreams.website.project_softdreams_restful_api.service;

import softdreams.website.project_softdreams_restful_api.domain.Product;

public interface OrderDetailService<T, V> {
    T createByOrderDetail(T t);
    T updateByOrderDetail(V object, long id);
    T updateByOrderDetail(Product product, long id);
    T updateByOrderDetailForSefl(T object);
    T fetchOrderDetailById(long id);
}

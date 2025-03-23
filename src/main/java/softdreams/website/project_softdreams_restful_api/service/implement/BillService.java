package softdreams.website.project_softdreams_restful_api.service.implement;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import softdreams.website.project_softdreams_restful_api.domain.Order;
import softdreams.website.project_softdreams_restful_api.dto.response.BillItem;
import softdreams.website.project_softdreams_restful_api.dto.response.BillRes;
import softdreams.website.project_softdreams_restful_api.service.OrderService;

@Service
public class BillService {
    @Autowired
    private OrderService orderService;
    
    public byte[] exportBillToPDF(long id) throws JRException {
        InputStream reportStream = getClass().getResourceAsStream("/templates/bill3.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        Order orderById = this.orderService.fetchOrderById(id);

        List<BillItem> items = orderById.getOrderDetails().stream().map(orderDetail -> {
            BillItem item = new BillItem();
            item.setNameProduct(orderDetail.getProduct().getName());
            item.setPriceProduct(orderDetail.getProduct().getPrice());
            item.setQuantity(orderDetail.getQuantity());
            item.setPriceOrderDetail(orderDetail.getPrice());
            return item;
        }).collect(Collectors.toList());

        double total = items.stream().mapToDouble(BillItem::getPriceOrderDetail).sum();
        double discount = 0;
        double personPay = total - discount;
        BillRes bill = new BillRes();
        bill.setReceiverName(orderById.getReceiverName());
        bill.setBillItems(items);
        bill.setTotalPrice(total);
        bill.setDiscount(discount);
        bill.setPayment(0);
        bill.setPersonPay(personPay);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);

        Map<String, Object> params = new HashMap<>();
        params.put("receiverName", bill.getReceiverName());
        params.put("totalPrice", bill.getTotalPrice());
        params.put("discount", bill.getDiscount());
        params.put("payment", bill.getPayment());
        params.put("personPay", bill.getPersonPay());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}

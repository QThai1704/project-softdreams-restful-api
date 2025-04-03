package softdreams.website.project_softdreams_restful_api.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductReq {
    private long id;
    @NotBlank(message = "Tên sản phẩm không được trống")
    @Size(min = 2, max = 50, message = "Tên sản phẩm phải từ 2 đến 100 ký tự")
    private String name;
    @NotNull(message = "Giá không được để trống")
    @PositiveOrZero(message = "Giá không được nhỏ hơn 0")
    private double price;
    private String image;
    @Column(columnDefinition = "MEDIUMTEXT")
    @Size(max = 16777215, message = "Chi tiết sản phẩm không được quá 16777215 ký tự")
    private String detailDesc;
    @Size(max = 255, message = "Mô tả ngắn không được quá 255 ký tự")
    private String shortDesc;
    @NotNull(message = "Số lượng không được để trống")
    @PositiveOrZero(message = "Số lượng không được nhỏ hơn 0")
    private long quantity;
}

package softdreams.website.project_softdreams_restful_api.domain;

import java.util.stream.Stream;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Tên sản phẩm phải từ 2 đến 100 ký tự")
    private String name;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Số lượng không được nhỏ hơn 0")
    private double price;

    private String image;

    @Column(columnDefinition = "MEDIUMTEXT")
    @Size(max = 16777215, message = "Mô tả chi tiết sản phẩm không được vượt quá 16777215 ký tự")
    private String detailDesc;

    @Size(max = 255, message = "Mô tả ngắn không được vượt quá 255 ký tự")
    private String shortDesc;
    
    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Số lượng không được nhỏ hơn 0")
    private long quantity;
}

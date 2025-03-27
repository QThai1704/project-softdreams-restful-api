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
    @NotBlank(message = "Product code is required")
    @Size(min = 2, max = 50, message = "Product code must be between 2 and 50 characters")
    private String productCode;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Category is required")
    @Positive(message = "Price must be greater than 0")
    private double price;

    @Size(max = 255, message = "Image URL must not exceed 255 characters")
    private String image;

    @Column(columnDefinition = "MEDIUMTEXT")
    @Size(max = 16777215, message = "Detail description must not exceed MEDIUMTEXT limit")
    private String detailDesc;

    @Size(max = 255, message = "Short description must not exceed 255 characters")
    private String shortDesc;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private long quantity;
}

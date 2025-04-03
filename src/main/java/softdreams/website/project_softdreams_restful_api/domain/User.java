package softdreams.website.project_softdreams_restful_api.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Họ và tên không được trống")
    private String fullName;
    
    @NotBlank(message = "Email is không được trống")
    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String address;

    @Pattern(regexp = "^(\\+84|0)[35789][0-9]{8}$", message = "Số điện thoại không hợp lệ")
    private String phone;
    private String avatar;

    private String refreshToken;
    

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    // @OneToOne(mappedBy = "user")
    // @JsonIgnore
    // private Cart cart;
}

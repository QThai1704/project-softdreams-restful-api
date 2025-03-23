package softdreams.website.project_softdreams_restful_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import softdreams.website.project_softdreams_restful_api.domain.Role;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.LoginReq;
import softdreams.website.project_softdreams_restful_api.dto.request.RegisterReq;
import softdreams.website.project_softdreams_restful_api.dto.response.LoginRes;
import softdreams.website.project_softdreams_restful_api.dto.response.ResGlobal;
import softdreams.website.project_softdreams_restful_api.service.RoleService;
import softdreams.website.project_softdreams_restful_api.service.UserService;
import softdreams.website.project_softdreams_restful_api.util.ApiMessage;
import softdreams.website.project_softdreams_restful_api.util.JwtService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AuthController {

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long jwtRefreshExpiration;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @PostMapping("/auth/login")
    @ApiMessage(message = "Đăng nhập")
    public ResponseEntity<LoginRes> login(@RequestBody LoginReq loginReq) {
        
        // Lấy thông tin từ request người dùng gửi xuống
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                    new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword());
        log.info("UsernamePasswordAuthenticationToken: {}", usernamePasswordAuthenticationToken);

        // Xác thực người dùng
        Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(usernamePasswordAuthenticationToken);
        log.info("Authentication: {}", authentication);

        // B3: Lưu các thông tin và SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Khởi tạo đối tượng trả về
        LoginRes loginRes = new LoginRes();
        LoginRes.LoginUser loginUser = new LoginRes.LoginUser();
        Optional<User> user = userService.findUserByEmail(loginReq.getEmail());
        if (user.isPresent()){
            loginUser.setId(user.get().getId());
            loginUser.setEmail(user.get().getEmail());
            loginUser.setRole(user.get().getRole());
            loginRes.setUser(loginUser);
        }

        // Tạo access token 
        String jwtAccessToken = jwtService.createAccessToken(authentication.getName());
        log.info("Access token {}", jwtAccessToken);
        loginRes.setAccessToken(jwtAccessToken);
        
        // Tạo refresh token
        String jwtRefreshToken = jwtService.createRefreshToken(loginReq.getEmail());
        log.info("Refresh token {}", jwtRefreshToken);

        // Tạo cookie và lưu lại token
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", jwtRefreshToken)
                                .httpOnly(true)
                                .secure(true) // chỉ có tác dụng cho http hoặc https, trong trường hợp này không có tác
                                              // dụng
                                .maxAge(jwtRefreshExpiration)
                                .path("/")
                                .build();
        log.info("Response Cookie", responseCookie);
        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
            .body(loginRes);
    }
    
    @PostMapping("auth/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterReq registerReq) {
        boolean isExistEmail = this.userService.isExistEmail(registerReq.getEmail());
        if (isExistEmail) {
            throw new UnsupportedOperationException("Email đã tồn tại");  
        }
        if (!registerReq.getPassword().equals(registerReq.getConfirmPassword())) {
            throw new UnsupportedOperationException("Mật khẩu không khớp");
        }
        User newUser = new User();
        String fullName = registerReq.getFirstName() + " " + registerReq.getLastName();
        String password = passwordEncoder.encode(registerReq.getPassword());
        newUser.setFullName(fullName);
        newUser.setEmail(registerReq.getEmail());
        newUser.setPassword(password);
        Role role = roleService.getRoleByName("USER").get();
        if(role == null) {
            throw new UnsupportedOperationException("Role không tồn tại");
        }
        newUser.setRole(role);
        this.userService.createUser(newUser);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đăng ký thành công!");
        return ResponseEntity.ok().body(response);
    }
}

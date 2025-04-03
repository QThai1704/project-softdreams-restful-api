package softdreams.website.project_softdreams_restful_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfigurationSource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import softdreams.website.project_softdreams_restful_api.domain.Role;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.LoginReq;
import softdreams.website.project_softdreams_restful_api.dto.request.RegisterReq;
import softdreams.website.project_softdreams_restful_api.dto.response.LoginRes;
import softdreams.website.project_softdreams_restful_api.dto.response.ResGlobal;
import softdreams.website.project_softdreams_restful_api.dto.response.UserRes;
import softdreams.website.project_softdreams_restful_api.exception.CustomException;
import softdreams.website.project_softdreams_restful_api.exception.RegisterException;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AuthController {

    private final CorsConfigurationSource corsConfigurationSource;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long jwtRefreshExpiration;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService  jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    AuthController(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @PostMapping("/auth/login")
    @ApiMessage(message = "Đăng nhập")
    public ResponseEntity<LoginRes> login(@Valid @RequestBody LoginReq loginReq) throws CustomException {
        
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
        this.userService.updateRefreshToken(loginReq.getEmail(), jwtRefreshToken);
        log.info("Refresh token {}", jwtRefreshToken);

        // Tạo cookie và lưu lại token
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", jwtRefreshToken)
                                .httpOnly(true)
                                .secure(false) // chỉ có tác dụng cho http hoặc https, trong trường hợp này không có tác
                                              // dụng
                                .maxAge(jwtRefreshExpiration)
                                .sameSite("Lax")
                                .path("/")
                                .build();
        log.info("Response Cookie", responseCookie);
        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
            .body(loginRes);
    }
    
    @PostMapping("/auth/register")
    public ResponseEntity<UserRes> register(@Valid @RequestBody RegisterReq registerReq) throws RegisterException {
        boolean isExistEmail = this.userService.isExistEmail(registerReq.getEmail());
        if (isExistEmail) {
            throw new RegisterException("Email đã tồn tại");  
        }
        if (!registerReq.getPassword().equals(registerReq.getConfirmPassword())) {
            throw new RegisterException("Mật khẩu không khớp");
        }
        User newUser = new User();
        String fullName = registerReq.getFirstName() + " " + registerReq.getLastName();
        String password = passwordEncoder.encode(registerReq.getPassword());
        newUser.setFullName(fullName);
        newUser.setEmail(registerReq.getEmail());
        newUser.setPassword(password);
        Role role = roleService.getRoleByName("USER").get();
        if(role == null) {
            throw new RegisterException("Role không tồn tại");
        }
        newUser.setRole(role);
        this.userService.createUser(newUser);
        UserRes userRes = this.userService.ResUserCreate(newUser);
        return ResponseEntity.ok().body(userRes);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage(message = "Get user by refresh token")
    public ResponseEntity<LoginRes> getRefreshToken(
                    @CookieValue(name = "refreshToken", defaultValue = "abc") String refreshToken)
                    throws CustomException {
        if (refreshToken.equals("abc")) {
            throw new CustomException("Bạn không có token");
        }

        // Kiem tra token
        Jwt decodedToken = this.jwtService.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();

        // Kiểm tra thông tin người dùng với refresh token + email
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refreshToken, email);
        if (currentUser == null) {
            throw new CustomException("Token không hợp lệ");
        }

        // Khởi tạo đối tượng ResLoginDTO
        LoginRes res = new LoginRes();
        User user = this.userService.findUserByEmail(email).get();
        if (user != null) {
            LoginRes.LoginUser userLogin = new LoginRes.LoginUser(
                                user.getId(),
                                user.getEmail(),
                                user.getRole());
                res.setUser(userLogin);
        }

        // Tạo access token
        String accessToken = jwtService.createAccessToken(email);
        res.setAccessToken(accessToken);

        // Tạo refresh token
        String new_refresh_token = jwtService.createRefreshToken(email);
        this.userService.updateRefreshToken(email, new_refresh_token); 

        // Tạo cookie để lưu lại token
        ResponseCookie responseCookie = ResponseCookie
                        .from("refreshToken", new_refresh_token)
                        .httpOnly(true)
                        .secure(false) // chỉ có tác dụng cho https, trong trường hợp này không có tác
                                        // dụng
                        .maxAge(jwtRefreshExpiration)
                        .sameSite("Lax")
                        .path("/")
                        .build();
        return ResponseEntity
                        .ok()
                        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                        .body(res);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> postLogout(@RequestBody() String email) throws CustomException {
        this.userService.updateRefreshToken(email, null);
        // Xóa cookie
        ResponseCookie deleteCookie = ResponseCookie
                        .from("refreshToken", null)
                        .httpOnly(true)
                        .secure(false) 
                        .maxAge(0)
                        .path("/")
                        .build();
        return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                        .body(null);
    }
}

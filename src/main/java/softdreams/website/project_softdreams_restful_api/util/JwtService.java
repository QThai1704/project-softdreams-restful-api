package softdreams.website.project_softdreams_restful_api.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.Base64;

import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.service.UserService;

@Service
public class JwtService {
    @Autowired
    private UserService userService;

    public final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long jwtAccessExpiration;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long jwtRefreshExpiration;

    @Value("${jwt.base64-secret}")
    private String jwtToken;

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    // Tạo access token
    public String createAccessToken(String username) {
        User user = this.userService.findUserByEmail(username).get();

        Instant now = Instant.now();
        Instant validity = now.plus(jwtAccessExpiration, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(username)
            .claim("role", user.getRole().getName())
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    // Tạo refresh token
    public String createRefreshToken(String username) {
        User user = this.userService.findUserByEmail(username).get();
        Instant now = Instant.now();
        Instant validity = now.plus(jwtRefreshExpiration, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(username)
            .claim("role", user.getRole().getName())
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }


    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtToken).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    // Kiểm tra giá trị access token
    public Jwt checkValidRefreshToken(String token) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            System.out.println(">>> Refresh Token error: " + e.getMessage());
            throw e;
        }
    }
}

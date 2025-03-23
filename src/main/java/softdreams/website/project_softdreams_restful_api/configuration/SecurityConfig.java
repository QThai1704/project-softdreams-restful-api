package softdreams.website.project_softdreams_restful_api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                    CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        http
            .csrf(c -> c.disable())
            .cors(Customizer.withDefaults())
            .httpBasic(hp -> hp.disable())
            .authorizeHttpRequests(
                            authz -> authz
                                .requestMatchers("/**", "/api/v1/auth/**")
                                .permitAll() 
                                .anyRequest().authenticated())
            .formLogin(f -> f.disable())
            .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
                            .authenticationEntryPoint(customAuthenticationEntryPoint));
        return http.build();
    }
}

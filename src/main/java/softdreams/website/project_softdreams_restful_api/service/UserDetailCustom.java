package softdreams.website.project_softdreams_restful_api.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import softdreams.website.project_softdreams_restful_api.repository.UserRepository;

@Component
public class UserDetailCustom implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        softdreams.website.project_softdreams_restful_api.domain.User userByEmail;
        Optional<softdreams.website.project_softdreams_restful_api.domain.User> user = userRepository.findByEmail(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Tài khoản hoặc mật khẩu không đúng");
        }
        userByEmail = user.get();
        return new User(
                userByEmail.getEmail(),
                userByEmail.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}

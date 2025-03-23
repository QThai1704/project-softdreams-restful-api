package softdreams.website.project_softdreams_restful_api.service.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import softdreams.website.project_softdreams_restful_api.domain.Role;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.UserReqCreate;
import softdreams.website.project_softdreams_restful_api.dto.request.UserReqUpdate;
import softdreams.website.project_softdreams_restful_api.dto.response.UserRes;
import softdreams.website.project_softdreams_restful_api.repository.RoleRepository;
import softdreams.website.project_softdreams_restful_api.repository.UserRepository;
import softdreams.website.project_softdreams_restful_api.service.UserService;

@Service
public class IUserService implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserReqCreate userReq) {
        User newUser = new User();
        newUser.setFullName(userReq.getFullName());
        newUser.setPassword(passwordEncoder.encode(userReq.getPassword()));
        newUser.setEmail(userReq.getEmail());
        newUser.setPhone(userReq.getPhone());
        newUser.setAddress(userReq.getAddress());
        newUser.setAvatar(userReq.getAvatar());
        Role role = this.roleRepository.findByName(userReq.getRole()).get();
        newUser.setRole(role);
        return this.userRepository.save(newUser);
    }

    @Override
    public UserRes ResUserCreate(User newUser) {
        UserRes userRes = new UserRes();
        UserRes.Role role = new UserRes.Role();
        userRes.setFullName(newUser.getFullName());
        userRes.setEmail(newUser.getEmail());
        userRes.setPhone(newUser.getPhone());
        userRes.setAddress(newUser.getAddress());
        userRes.setAvatar(newUser.getAvatar());
        role.setName(newUser.getRole().getName());
        userRes.setRole(role);
        return userRes;
    }

    @Override
    public List<User> fetchAllUser() {
        return this.userRepository.findAll();
    }

    @Override
    public List<UserRes> fetchAllUserRes() {
        List<User> users = this.fetchAllUser();
        List<UserRes> userResList = users.stream().map(user -> {
            UserRes userResItem = new UserRes();
            UserRes.Role role = new UserRes.Role();
            userResItem.setId(user.getId());
            userResItem.setFullName(user.getFullName());
            userResItem.setEmail(user.getEmail());
            userResItem.setPhone(user.getPhone());
            userResItem.setAddress(user.getAddress());
            userResItem.setAvatar(user.getAvatar());
            role.setName(user.getRole().getName());
            userResItem.setRole(role);
            return userResItem;
        }).toList();
        return userResList;
    }

    @Override
    public Optional<User> findUserById(long id) {
        return this.userRepository.findById(id);
    }
    
    @Override
    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public UserRes fetchUserById(long id) {
        User userById = this.findUserById(id).get();
        if (userById == null) {
            throw new UnsupportedOperationException("User not found");
        }
        UserRes userRes = new UserRes();
        UserRes.Role role = new UserRes.Role();
        userRes.setId(userById.getId());
        userRes.setFullName(userById.getFullName());
        userRes.setEmail(userById.getEmail());
        userRes.setPhone(userById.getPhone());
        userRes.setAddress(userById.getAddress());
        userRes.setAvatar(userById.getAvatar());
        role.setName(userById.getRole().getName());
        userRes.setRole(role);
        return userRes;
    }

    @Override
    public User updateUser(UserReqUpdate userReqUpdate) {
        User currentUser = this.findUserById(userReqUpdate.getId()).get();
        if (currentUser == null) {
            throw new UnsupportedOperationException("User not found");
        }
        Role role = this.roleRepository.findByName(userReqUpdate.getRole()).get();
        currentUser.setFullName(userReqUpdate.getFullName());
        currentUser.setEmail(userReqUpdate.getEmail());
        currentUser.setPhone(userReqUpdate.getPhone());
        currentUser.setAddress(userReqUpdate.getAddress());
        currentUser.setAvatar(userReqUpdate.getAvatar());
        role.setName(userReqUpdate.getRole());
        currentUser.setRole(role);
        return this.userRepository.save(currentUser);
    }

    @Override
    public UserRes ResUserUpdate(UserReqUpdate userReqUpdate) {
        User updatedUser = this.updateUser(userReqUpdate);
        // Phản hồi
        UserRes userRes = new UserRes();
        UserRes.Role roleRes = new UserRes.Role();
        userRes.setId(updatedUser.getId());
        userRes.setFullName(updatedUser.getFullName());
        userRes.setEmail(updatedUser.getEmail());
        userRes.setPhone(updatedUser.getPhone());
        userRes.setAddress(updatedUser.getAddress());
        userRes.setAvatar(updatedUser.getAvatar());
        roleRes.setName(updatedUser.getRole().getName());
        userRes.setRole(roleRes);
        return userRes;
    }

    @Override
    public void deleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public boolean isExistEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public User createUser(User user) {
        return this.userRepository.save(user);
    }
}

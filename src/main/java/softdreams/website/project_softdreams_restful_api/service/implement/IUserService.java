package softdreams.website.project_softdreams_restful_api.service.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.CartDetail;
import softdreams.website.project_softdreams_restful_api.domain.Order;
import softdreams.website.project_softdreams_restful_api.domain.OrderDetail;
import softdreams.website.project_softdreams_restful_api.domain.Role;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.UserReqCreate;
import softdreams.website.project_softdreams_restful_api.dto.request.UserReqUpdate;
import softdreams.website.project_softdreams_restful_api.dto.response.UserRes;
import softdreams.website.project_softdreams_restful_api.dto.response.UserSelectForAdmin;
import softdreams.website.project_softdreams_restful_api.exception.CustomException;
import softdreams.website.project_softdreams_restful_api.repository.CartDetailRepository;
import softdreams.website.project_softdreams_restful_api.repository.CartRepository;
import softdreams.website.project_softdreams_restful_api.repository.OrderDetailRepository;
import softdreams.website.project_softdreams_restful_api.repository.OrderRepository;
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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Override
    public User createUser(UserReqCreate userReq) throws CustomException {
        // Kiểm tra email đã tồn tại chưa
        if (this.isExistEmail(userReq.getEmail())) {
            throw new CustomException("Email đã tồn tại!");
        }
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
    public User updateUser(UserReqUpdate userReqUpdate) throws CustomException {
        User currentUser = this.findUserById(userReqUpdate.getId()).get();
        if (currentUser == null) {
            throw new CustomException("Không tìm thấy người dùng!");
        }
        if(!currentUser.getEmail().equals(userReqUpdate.getEmail())) {
            if (this.isExistEmail(userReqUpdate.getEmail())) {
                throw new CustomException("Email đã tồn tại!");
            }
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
    public int UserUpdateById(UserReqUpdate userReqUpdate) throws CustomException {
        Role roleFindById = this.roleRepository.findByName(userReqUpdate.getRole()).get();
        return this.userRepository.updateUserById(
            userReqUpdate.getId(), 
            userReqUpdate.getFullName(), 
            userReqUpdate.getEmail(), 
            userReqUpdate.getAddress(), 
            userReqUpdate.getPhone(), 
            userReqUpdate.getAvatar(), 
            (long) roleFindById.getId()
        );
    }

    @Override
    public UserRes ResUserUpdate(UserReqUpdate userReqUpdate) throws CustomException {
        int result = this.UserUpdateById(userReqUpdate);
        if(result == 0) {
            throw new CustomException("Cập nhật không thành công!");
        }
        User updatedUser = this.findUserById(userReqUpdate.getId()).get();
        if(updatedUser == null) {
            throw new CustomException("Không tìm thấy người dùng!");
        }
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
    @Transactional
    public void deleteUser(long id) throws CustomException {
        User user = this.findUserById(id).get();
        if (user == null) {
            throw new CustomException("Không tìm thấy người dùng!");
        }
        if(user.getRole().getName().equals("ADMIN")) {
            if(this.userRepository.countUserByAdmin() == 1) {
                throw new CustomException("Bạn là người quản trị cuối cùng của hệ thống, không thể xóa!");
            }
        }
        
        List<Order> orders = this.orderRepository.findByUserId(id);
        for (Order order : orders) {
            List<OrderDetail> orderDetails = this.orderDetailRepository.findAllOrderDetailsByOrderId(order.getId());
            for (OrderDetail orderDetail : orderDetails) {
                orderDetail.setOrder(null);
                this.orderDetailRepository.save(orderDetail);
                this.orderDetailRepository.deleteById(orderDetail.getId());
            }
            order.setUser(null);
            this.orderRepository.save(order);
            this.orderRepository.deleteById(order.getId());
        }
        
        List<Cart> carts = this.cartRepository.findByUserId(id);
        for (Cart cart : carts) {
            List<CartDetail> cartDetails = this.cartDetailRepository.findAllCartDetailsByCartId(cart.getId());
            for (CartDetail cartDetail : cartDetails) {
                cartDetail.setCart(null);
                this.cartDetailRepository.save(cartDetail);
                this.cartDetailRepository.deleteById(cartDetail.getId());
            }
            cart.setUser(null);
            this.cartRepository.save(cart);
            this.cartRepository.deleteById(cart.getId());
        }
        
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

    // Fetch user by token and email
    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    // Update refresh token
    public User updateRefreshToken(String email, String refreshToken) throws CustomException {
        
        User user = this.findUserByEmail(email).get();
        if (user == null) {
            throw new CustomException("Người dùng không tồn tại!");
        }
        user.setRefreshToken(refreshToken);
        return this.userRepository.save(user);
    }

    @Override
    public long getQuantityUserByAdmin() {
        return this.userRepository.countUserByAdmin();
    }

    @Override
    public List<UserSelectForAdmin> getAllUserSelectForAdmin() {
        return this.userRepository.getAllUserSelectForAdmin();
    }

    
}

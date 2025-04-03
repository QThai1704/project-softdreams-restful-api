package softdreams.website.project_softdreams_restful_api.service;

import java.util.List;
import java.util.Optional;

import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.UserReqCreate;
import softdreams.website.project_softdreams_restful_api.dto.request.UserReqUpdate;
import softdreams.website.project_softdreams_restful_api.dto.response.UserRes;
import softdreams.website.project_softdreams_restful_api.dto.response.UserSelectForAdmin;
import softdreams.website.project_softdreams_restful_api.exception.CustomException;

public interface UserService {
    // Done
    User createUser(User user);
    User createUser(UserReqCreate user) throws CustomException;
    UserRes ResUserCreate(User newUser);
    // No Done
    List<User> fetchAllUser();
    List<UserRes> fetchAllUserRes();
    // Done
    Optional<User> findUserById(long id);
    UserRes fetchUserById(long id);
    Optional<User> findUserByEmail(String email);

    // Update sử dụng native query
    int UserUpdateById(
        UserReqUpdate userReqUpdate
    ) throws CustomException;

    User updateUser(UserReqUpdate userReqUpdate) throws CustomException;
    UserRes ResUserUpdate(UserReqUpdate userReqUpdate) throws CustomException;

    void deleteUser(long id) throws CustomException;

    boolean isExistEmail(String email);

    User getUserByRefreshTokenAndEmail(String token, String email);

    User updateRefreshToken(String email, String refreshToken) throws CustomException;

    long getQuantityUserByAdmin();

    List<UserSelectForAdmin> getAllUserSelectForAdmin();
}

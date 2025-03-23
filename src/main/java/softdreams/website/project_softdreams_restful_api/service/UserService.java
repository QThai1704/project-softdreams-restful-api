package softdreams.website.project_softdreams_restful_api.service;

import java.util.List;
import java.util.Optional;

import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.UserReqCreate;
import softdreams.website.project_softdreams_restful_api.dto.request.UserReqUpdate;
import softdreams.website.project_softdreams_restful_api.dto.response.UserRes;

public interface UserService {
    // Done
    User createUser(User user);
    User createUser(UserReqCreate user);
    UserRes ResUserCreate(User newUser);
    // No Done
    List<User> fetchAllUser();
    List<UserRes> fetchAllUserRes();
    // Done
    Optional<User> findUserById(long id);
    UserRes fetchUserById(long id);
    Optional<User> findUserByEmail(String email);

    // Done
    User updateUser(UserReqUpdate userReqUpdate);
    UserRes ResUserUpdate(UserReqUpdate userReqUpdate);

    void deleteUser(long id);

    boolean isExistEmail(String email);
}

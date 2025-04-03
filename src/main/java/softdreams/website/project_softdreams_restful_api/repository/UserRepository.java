package softdreams.website.project_softdreams_restful_api.repository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.UserReqCreate;
import softdreams.website.project_softdreams_restful_api.dto.response.UserSelectForAdmin;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(UserReqCreate userReq);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User findByRefreshTokenAndEmail(String token, String email);
    @Query(value = "SELECT count(users.role_id) as coutUserByAdmin FROM users WHERE role_id = 1;", nativeQuery = true)
    long countUserByAdmin();

    @Query(value = "SELECT id, full_name, email FROM users", nativeQuery = true)
    List<UserSelectForAdmin> getAllUserSelectForAdmin();

    @Modifying 
    @Transactional
    @Query(value = """
        UPDATE users 
        SET full_name = :fullName, email = :email, address = :address, phone = :phone, avatar = :avatar, role_id = :roleId
        WHERE id = :id
    """, nativeQuery = true)
    int updateUserById(
        @Param("id") long id, 
        @Param("fullName") String fullName, 
        @Param("email") String email, 
        @Param("address") String address, 
        @Param("phone") String phone, 
        @Param("avatar") String avatar, 
        @Param("roleId") long roleId
    );
}

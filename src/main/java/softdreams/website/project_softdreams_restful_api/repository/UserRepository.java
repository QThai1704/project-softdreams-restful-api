package softdreams.website.project_softdreams_restful_api.repository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.UserReqCreate;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User save(UserReqCreate userReq);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

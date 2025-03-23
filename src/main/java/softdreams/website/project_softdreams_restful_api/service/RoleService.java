package softdreams.website.project_softdreams_restful_api.service;

import java.util.Optional;

import softdreams.website.project_softdreams_restful_api.domain.Role;

public interface RoleService {
    Optional<Role> getRoleByName(String name);   
}

package softdreams.website.project_softdreams_restful_api.service.implement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import softdreams.website.project_softdreams_restful_api.domain.Role;
import softdreams.website.project_softdreams_restful_api.repository.RoleRepository;
import softdreams.website.project_softdreams_restful_api.service.RoleService;

@Service
public class IRoleService implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Role> getRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }

    
}

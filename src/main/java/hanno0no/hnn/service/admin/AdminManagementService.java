package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.response.admin.AdminUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminManagementService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AdminUserResponse> getAdmins() {
        List<AdminUserResponse> responses = new ArrayList<>();
        for (AdminUser admin : adminUserRepository.findAll()) {
            responses.add(new AdminUserResponse(
                    admin.getAdminId(),
                    admin.getUserName(),
                    new ArrayList<>(admin.getWorkAreas()),
                    admin.getPassword_hash() != null
            ));
        }
        return responses;
    }
}

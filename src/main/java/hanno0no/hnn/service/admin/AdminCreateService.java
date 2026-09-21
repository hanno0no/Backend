package hanno0no.hnn.service.admin;


import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.request.admin.AdminCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AdminCreateService {

    private final AdminUserRepository adminUserRepository;

    @Transactional
    public int createAdmin(AdminCreateRequest request) {

        if (adminUserRepository.existsByUserName(request.getUserName())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        AdminUser adminUser = new AdminUser();
        adminUser.setUserName(request.getUserName());
        // 비밀번호는 첫 로그인 시 본인이 설정한다 (AdminSetupPasswordService 참고).
        adminUser.setPassword_hash(null);
        adminUser.setRole("admin");
        adminUser.setWorkAreas(request.getWorkAreas() != null ? new HashSet<>(request.getWorkAreas()) : new HashSet<>());

        AdminUser savedAdmin = adminUserRepository.save(adminUser);

        return savedAdmin.getAdminId();

    }

}

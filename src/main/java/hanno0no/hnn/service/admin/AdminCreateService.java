package hanno0no.hnn.service.admin;


import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.request.admin.AdminCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminCreateService {

    private static final Set<String> ALLOWED_WORK_AREAS = Set.of("접수", "디자인", "출력", "기타");

    private final AdminUserRepository adminUserRepository;

    @Transactional
    public int createAdmin(AdminCreateRequest request) {

        if (!StringUtils.hasText(request.getUserName())) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }

        if (adminUserRepository.existsByUserName(request.getUserName())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if (request.getWorkAreas() != null) {
            for (String area : request.getWorkAreas()) {
                if (!ALLOWED_WORK_AREAS.contains(area)) {
                    throw new IllegalArgumentException("허용되지 않는 업무 담당 영역입니다: " + area);
                }
            }
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

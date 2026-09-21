package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.exception.ForbiddenException;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.request.admin.AdminUserUpdateRequest;
import hanno0no.hnn.response.admin.AdminUserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
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

    @Transactional
    public void updateAdmin(int targetAdminId, AdminUserUpdateRequest request, AdminUser currentAdmin) {
        AdminUser target = adminUserRepository.findById(targetAdminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없습니다: " + targetAdminId));

        if (StringUtils.hasText(request.getPassword()) && targetAdminId != currentAdmin.getAdminId()) {
            throw new ForbiddenException("본인 계정만 비밀번호를 변경할 수 있습니다.");
        }

        if (StringUtils.hasText(request.getUserName()) && !request.getUserName().equals(target.getUserName())) {
            if (adminUserRepository.existsByUserName(request.getUserName())) {
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            }
            target.setUserName(request.getUserName());
        }

        if (StringUtils.hasText(request.getPassword())) {
            target.setPassword_hash(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getWorkAreas() != null) {
            target.setWorkAreas(new HashSet<>(request.getWorkAreas()));
        }
    }
}

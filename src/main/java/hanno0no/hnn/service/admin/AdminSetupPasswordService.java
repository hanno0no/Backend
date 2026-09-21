package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.exception.UnauthorizedException;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.request.admin.AdminSetupPasswordRequest;
import hanno0no.hnn.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AdminSetupPasswordService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public String setupPassword(AdminSetupPasswordRequest request) {
        AdminUser adminUser = adminUserRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new UnauthorizedException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (adminUser.getPassword_hash() != null) {
            throw new IllegalStateException("이미 비밀번호가 설정된 계정입니다.");
        }
        if (!StringUtils.hasText(request.getNewPassword())) {
            throw new IllegalArgumentException("새 비밀번호를 입력해주세요.");
        }

        adminUser.setPassword_hash(passwordEncoder.encode(request.getNewPassword()));
        return jwtUtil.generateToken(adminUser.getUserName());
    }
}

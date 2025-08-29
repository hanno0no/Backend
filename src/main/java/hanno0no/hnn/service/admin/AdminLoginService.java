package hanno0no.hnn.service.admin;


import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.request.admin.AdminLoginRequest;
import hanno0no.hnn.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLoginService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(AdminLoginRequest request) {

        AdminUser adminUser = adminUserRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), adminUser.getPassword_hash())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.generateToken(adminUser.getUserName());

    }



}

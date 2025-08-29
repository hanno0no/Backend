package hanno0no.hnn.service.admin;


import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.request.admin.AdminCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCreateService {

    private final PasswordEncoder passwordEncoder;
    private final AdminUserRepository adminUserRepository;

    @Transactional
    public int createAdmin(AdminCreateRequest request) {

        if (adminUserRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 1. 클라이언트로부터 받은 원본 비밀번호를 가져옵니다.
        String rawPassword = request.getPassword();
        // 2. PasswordEncoder를 사용해 원본 비밀번호를 해시(암호화)합니다.
        String encodedPassword = passwordEncoder.encode(rawPassword);

        AdminUser adminUser = new AdminUser();

        adminUser.setUserName(request.getUserName());
        adminUser.setPassword_hash(encodedPassword);
        adminUser.setRole(request.getRole());

        AdminUser savedAdmin = adminUserRepository.save(adminUser);

        return savedAdmin.getAdminId();

    }

}

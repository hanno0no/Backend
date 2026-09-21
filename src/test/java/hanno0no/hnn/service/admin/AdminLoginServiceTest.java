package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.exception.PasswordNotSetException;
import hanno0no.hnn.exception.UnauthorizedException;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.request.admin.AdminLoginRequest;
import hanno0no.hnn.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AdminLoginServiceTest {

    @Mock AdminUserRepository adminUserRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtUtil jwtUtil;

    @InjectMocks AdminLoginService adminLoginService;

    private AdminLoginRequest requestFor(String username, String password) {
        // AdminLoginRequest는 setter가 없으므로 리플렉션 대신 생성자/필드 접근이 필요하면
        // 테스트 전용 헬퍼가 필요하다. 여기서는 Jackson과 동일한 방식(필드 직접 설정)을 리플렉션으로 흉내내지 않고
        // AdminLoginRequest에 테스트에서만 쓰는 정적 팩토리를 두지 않는 대신, 아래처럼 직접 필드를 설정한다.
        return TestRequestFactory.loginRequest(username, password);
    }

    @Test
    void loginWithNullPasswordHashThrowsPasswordNotSet() {
        AdminUser admin = new AdminUser();
        admin.setUserName("newbie");
        admin.setPassword_hash(null);
        when(adminUserRepository.findByUserName("newbie")).thenReturn(Optional.of(admin));

        assertThrows(PasswordNotSetException.class,
                () -> adminLoginService.login(requestFor("newbie", "anything")));
    }

    @Test
    void loginWithWrongPasswordThrowsUnauthorized() {
        AdminUser admin = new AdminUser();
        admin.setUserName("한수민");
        admin.setPassword_hash("hashed");
        when(adminUserRepository.findByUserName("한수민")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> adminLoginService.login(requestFor("한수민", "wrong")));
    }

    @Test
    void loginSuccessReturnsToken() {
        AdminUser admin = new AdminUser();
        admin.setUserName("한수민");
        admin.setPassword_hash("hashed");
        when(adminUserRepository.findByUserName("한수민")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("correct", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken("한수민")).thenReturn("jwt-token");

        String token = adminLoginService.login(requestFor("한수민", "correct"));

        assertEquals("jwt-token", token);
    }
}

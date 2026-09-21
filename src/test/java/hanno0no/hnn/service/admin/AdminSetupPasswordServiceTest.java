package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.request.admin.AdminSetupPasswordRequest;
import hanno0no.hnn.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminSetupPasswordServiceTest {

    @Mock AdminUserRepository adminUserRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtUtil jwtUtil;

    @InjectMocks AdminSetupPasswordService adminSetupPasswordService;

    @Test
    void setsPasswordAndReturnsTokenWhenNoneSet() {
        AdminUser admin = new AdminUser();
        admin.setUserName("newbie");
        admin.setPassword_hash(null);
        when(adminUserRepository.findByUserName("newbie")).thenReturn(Optional.of(admin));
        when(passwordEncoder.encode("myNewPass1!")).thenReturn("encoded");
        when(jwtUtil.generateToken("newbie")).thenReturn("jwt-token");

        AdminSetupPasswordRequest request = new AdminSetupPasswordRequest();
        request.setUserName("newbie");
        request.setNewPassword("myNewPass1!");

        String token = adminSetupPasswordService.setupPassword(request);

        assertEquals("jwt-token", token);
        assertEquals("encoded", admin.getPassword_hash());
    }

    @Test
    void rejectsWhenPasswordAlreadySet() {
        AdminUser admin = new AdminUser();
        admin.setUserName("한수민");
        admin.setPassword_hash("already-hashed");
        when(adminUserRepository.findByUserName("한수민")).thenReturn(Optional.of(admin));

        AdminSetupPasswordRequest request = new AdminSetupPasswordRequest();
        request.setUserName("한수민");
        request.setNewPassword("tryToHijack1!");

        assertThrows(IllegalStateException.class,
                () -> adminSetupPasswordService.setupPassword(request));
    }
}

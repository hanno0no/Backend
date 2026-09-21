package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.response.admin.AdminUserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminManagementServiceTest {

    @Mock AdminUserRepository adminUserRepository;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks AdminManagementService adminManagementService;

    @Test
    void listsAdminsWithPasswordSetFlag() {
        AdminUser withPassword = new AdminUser();
        withPassword.setAdminId(1);
        withPassword.setUserName("한수민");
        withPassword.setPassword_hash("hashed");
        withPassword.setWorkAreas(Set.of("디자인"));

        AdminUser withoutPassword = new AdminUser();
        withoutPassword.setAdminId(2);
        withoutPassword.setUserName("신입");
        withoutPassword.setPassword_hash(null);
        withoutPassword.setWorkAreas(Set.of());

        when(adminUserRepository.findAll()).thenReturn(List.of(withPassword, withoutPassword));

        List<AdminUserResponse> result = adminManagementService.getAdmins();

        assertEquals(2, result.size());
        assertTrue(result.get(0).isPasswordSet());
        assertEquals(List.of("디자인"), result.get(0).getWorkAreas());
        assertFalse(result.get(1).isPasswordSet());
    }
}

package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.exception.ForbiddenException;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.request.admin.AdminUserUpdateRequest;
import hanno0no.hnn.response.admin.AdminUserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
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

    @Test
    void updatingOthersPasswordThrowsForbidden() {
        AdminUser me = new AdminUser();
        me.setAdminId(1);
        me.setUserName("한수민");

        AdminUser target = new AdminUser();
        target.setAdminId(2);
        target.setUserName("김근희");
        target.setWorkAreas(Set.of());
        when(adminUserRepository.findById(2)).thenReturn(Optional.of(target));

        AdminUserUpdateRequest request = new AdminUserUpdateRequest();
        request.setPassword("hijack1!");

        assertThrows(ForbiddenException.class,
                () -> adminManagementService.updateAdmin(2, request, me));
    }

    @Test
    void updatingOwnPasswordSucceeds() {
        AdminUser me = new AdminUser();
        me.setAdminId(1);
        me.setUserName("한수민");
        me.setWorkAreas(Set.of());
        when(adminUserRepository.findById(1)).thenReturn(Optional.of(me));
        when(passwordEncoder.encode("newPass1!")).thenReturn("encoded");

        AdminUserUpdateRequest request = new AdminUserUpdateRequest();
        request.setPassword("newPass1!");

        adminManagementService.updateAdmin(1, request, me);

        assertEquals("encoded", me.getPassword_hash());
    }

    @Test
    void updatingWorkAreasDoesNotRequireOwnership() {
        AdminUser me = new AdminUser();
        me.setAdminId(1);
        me.setUserName("한수민");

        AdminUser target = new AdminUser();
        target.setAdminId(2);
        target.setUserName("김근희");
        target.setWorkAreas(Set.of());
        when(adminUserRepository.findById(2)).thenReturn(Optional.of(target));

        AdminUserUpdateRequest request = new AdminUserUpdateRequest();
        request.setWorkAreas(List.of("출력"));

        adminManagementService.updateAdmin(2, request, me);

        assertEquals(Set.of("출력"), target.getWorkAreas());
    }

    @Test
    void updatingToExistingUserNameThrows() {
        AdminUser me = new AdminUser();
        me.setAdminId(1);
        me.setUserName("한수민");
        me.setWorkAreas(Set.of());
        when(adminUserRepository.findById(1)).thenReturn(Optional.of(me));
        when(adminUserRepository.existsByUserName("김근희")).thenReturn(true);

        AdminUserUpdateRequest request = new AdminUserUpdateRequest();
        request.setUserName("김근희");

        assertThrows(IllegalArgumentException.class,
                () -> adminManagementService.updateAdmin(1, request, me));
    }
}

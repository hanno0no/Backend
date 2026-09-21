package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.request.admin.AdminCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminCreateServiceTest {

    @Mock AdminUserRepository adminUserRepository;

    @InjectMocks AdminCreateService adminCreateService;

    @Test
    void createsAdminWithoutPasswordAndFixedRole() {
        AdminCreateRequest request = new AdminCreateRequest();
        request.setUserName("newadmin");
        request.setWorkAreas(List.of("디자인", "출력"));

        when(adminUserRepository.existsByUserName("newadmin")).thenReturn(false);
        when(adminUserRepository.save(any(AdminUser.class))).thenAnswer(invocation -> {
            AdminUser saved = invocation.getArgument(0);
            saved.setAdminId(5);
            return saved;
        });

        int id = adminCreateService.createAdmin(request);

        assertEquals(5, id);
        ArgumentCaptor<AdminUser> captor = ArgumentCaptor.forClass(AdminUser.class);
        verify(adminUserRepository).save(captor.capture());
        AdminUser saved = captor.getValue();
        assertNull(saved.getPassword_hash());
        assertEquals("admin", saved.getRole());
        assertEquals(java.util.Set.of("디자인", "출력"), saved.getWorkAreas());
    }

    @Test
    void duplicateUserNameThrows() {
        AdminCreateRequest request = new AdminCreateRequest();
        request.setUserName("existing");
        when(adminUserRepository.existsByUserName("existing")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> adminCreateService.createAdmin(request));
    }

    @Test
    void blankUserNameThrows() {
        AdminCreateRequest request = new AdminCreateRequest();
        request.setUserName("   ");

        assertThrows(IllegalArgumentException.class, () -> adminCreateService.createAdmin(request));

        verify(adminUserRepository, org.mockito.Mockito.never()).existsByUserName(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void invalidWorkAreaThrowsOnCreate() {
        AdminCreateRequest request = new AdminCreateRequest();
        request.setUserName("newadmin");
        request.setWorkAreas(List.of("디자인", "잘못된영역"));

        when(adminUserRepository.existsByUserName("newadmin")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> adminCreateService.createAdmin(request));
    }
}

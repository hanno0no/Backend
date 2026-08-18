package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.domain.state.State;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import hanno0no.hnn.service.sse.SseEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUpdateServiceTest {

    @Mock OrdersRepository ordersRepository;
    @Mock StateRepository stateRepository;
    @Mock AdminUserRepository adminUserRepository;
    @Mock SseEventService sseEventService;

    @InjectMocks AdminUpdateService adminUpdateService;

    @Test
    void namedManagerMijijeongIsAssignedNotCleared() {
        Orders order = new Orders();
        AdminUser named = new AdminUser();
        named.setUserName("미지정");
        when(ordersRepository.findById(1)).thenReturn(Optional.of(order));
        when(adminUserRepository.findByUserName("미지정")).thenReturn(Optional.of(named));

        adminUpdateService.updateOrderManager(1, "미지정");

        assertEquals(named, order.getAdmin());
        verify(adminUserRepository).findByUserName("미지정");
    }

    @Test
    void nullManagerClearsAssignment() {
        Orders order = new Orders();
        order.setAdmin(new AdminUser());
        when(ordersRepository.findById(1)).thenReturn(Optional.of(order));

        adminUpdateService.updateOrderManager(1, null);

        assertNull(order.getAdmin());
        verifyNoInteractions(adminUserRepository);
    }

    @Test
    void statusChangeEmitsIndexUpdated() {
        Orders order = new Orders();
        State accepted = new State();
        accepted.setState("accepted");
        when(ordersRepository.findById(1)).thenReturn(Optional.of(order));
        when(stateRepository.findByState("accepted")).thenReturn(Optional.of(accepted));

        adminUpdateService.updateOrderStatus(1, "accepted");

        verify(sseEventService).emit(SseEventService.INDEX_UPDATED);
    }

    @Test
    void hideEmitsIndexUpdated() {
        Orders order = new Orders();
        when(ordersRepository.findById(1)).thenReturn(Optional.of(order));

        adminUpdateService.updateOrderHidden(1, true);

        verify(sseEventService).emit(SseEventService.INDEX_UPDATED);
    }
}

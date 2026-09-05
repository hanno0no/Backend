package hanno0no.hnn.service.register;

import hanno0no.hnn.domain.material.Material;
import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.domain.state.State;
import hanno0no.hnn.domain.team.Team;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.repository.material.MaterialRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import hanno0no.hnn.repository.team.TeamRepository;
import hanno0no.hnn.request.register.RegisterRequest;
import hanno0no.hnn.service.sse.SseEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock OrdersRepository ordersRepository;
    @Mock TeamRepository teamRepository;
    @Mock MaterialRepository materialRepository;
    @Mock StateRepository stateRepository;
    @Mock AdminUserRepository adminUserRepository;
    @Mock SseEventService sseEventService;

    @InjectMocks RegisterService registerService;

    @Test
    void createOrderEmitsOrdersUpdated() {
        Team team = new Team();
        team.setTeamNum("T1");
        Material material = new Material();
        material.setMaterialName("아크릴");
        State submitted = new State();
        submitted.setState("submitted");
        Orders saved = new Orders();
        saved.setOrderId(16);
        saved.setTeam(team);
        saved.setMaterial(material);

        when(teamRepository.findByTeamNum("T1")).thenReturn(Optional.of(team));
        when(materialRepository.findByMaterialName("아크릴")).thenReturn(Optional.of(material));
        when(stateRepository.findByState("submitted")).thenReturn(Optional.of(submitted));
        when(ordersRepository.save(any(Orders.class))).thenReturn(saved);

        RegisterRequest request = org.mockito.Mockito.mock(RegisterRequest.class);
        when(request.getTeamNum()).thenReturn("T1");
        when(request.getMaterial()).thenReturn("아크릴");

        registerService.createOrder(request);

        verify(sseEventService).emit(SseEventService.ORDERS_UPDATED);
    }
}

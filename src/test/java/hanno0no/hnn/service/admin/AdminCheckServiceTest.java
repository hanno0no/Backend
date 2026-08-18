package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.eventinfo.EventInfo;
import hanno0no.hnn.domain.material.Material;
import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.domain.state.State;
import hanno0no.hnn.domain.team.Team;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.repository.eventinfo.EventInfoRepository;
import hanno0no.hnn.repository.material.MaterialRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import hanno0no.hnn.request.admin.OrderSearchRequest;
import hanno0no.hnn.response.admin.AdminCheckResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminCheckServiceTest {

    @Mock OrdersRepository ordersRepository;
    @Mock MaterialRepository materialRepository;
    @Mock StateRepository stateRepository;
    @Mock AdminUserRepository adminUserRepository;
    @Mock EventInfoRepository eventInfoRepository;

    @InjectMocks AdminCheckService adminCheckService;

    LocalDateTime start = LocalDateTime.of(2026, 8, 1, 0, 0);
    LocalDateTime end = LocalDateTime.of(2026, 8, 31, 23, 59);

    @Test
    void filtersByMaterialAndTeamNumTogether() {
        stubOpenEvent();

        OrderSearchRequest request = new OrderSearchRequest();
        request.setMaterial("PLA");
        request.setTeamNum("T2_1");

        when(ordersRepository.findByFilters(isNull(), isNull(), eq(false), eq("PLA"), eq("T2_1"), eq(start), eq(end)))
                .thenReturn(List.of(order(1, "a.stl", "T2_1", "PLA", "accepted")));

        List<AdminCheckResponse> result = adminCheckService.getOrders(request);

        assertEquals(1, result.size());
        assertEquals("PLA", result.get(0).getMaterial());
        assertEquals("T2_1", result.get(0).getTeamNum());
    }

    @Test
    void filtersUnassignedAsNullAdmin() {
        stubOpenEvent();
        OrderSearchRequest request = new OrderSearchRequest();
        request.setManager("unassigned");

        when(ordersRepository.findByFilters(isNull(), isNull(), eq(true), isNull(), isNull(), eq(start), eq(end)))
                .thenReturn(List.of(order(1, "a.stl", "T2_1", "PLA", "accepted")));

        List<AdminCheckResponse> result = adminCheckService.getOrders(request);

        assertEquals(1, result.size());
        assertEquals("", result.get(0).getAdmin());
    }

    @Test
    void statusFilterKeepsEventDateWindow() {
        stubOpenEvent();
        when(stateRepository.findStateIdByState("accepted")).thenReturn(Optional.of(2));

        OrderSearchRequest request = new OrderSearchRequest();
        request.setStatus("accepted");

        when(ordersRepository.findByFilters(eq(2), isNull(), eq(false), isNull(), isNull(), eq(start), eq(end)))
                .thenReturn(List.of(order(1, "a.stl", "T2_1", "PLA", "accepted")));

        List<AdminCheckResponse> result = adminCheckService.getOrders(request);

        assertEquals(1, result.size());
        assertEquals("accepted", result.get(0).getState());
    }

    private void stubOpenEvent() {
        EventInfo event = new EventInfo();
        event.setStartTime(start);
        event.setEndTime(end);
        when(eventInfoRepository.findByIsOpen()).thenReturn(Optional.of(event));
    }

    private static Orders order(int id, String file, String teamNum, String materialName, String stateName) {
        Team team = new Team();
        team.setTeamNum(teamNum);
        Material material = new Material();
        material.setMaterialName(materialName);
        State state = new State();
        state.setState(stateName);
        Orders o = new Orders();
        o.setOrderId(id);
        o.setFileName(file);
        o.setTeam(team);
        o.setMaterial(material);
        o.setState(state);
        return o;
    }
}

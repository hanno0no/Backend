package hanno0no.hnn.service.admin;


import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.domain.eventinfo.EventInfo;
import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.repository.eventinfo.EventInfoRepository;
import hanno0no.hnn.repository.material.MaterialRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import hanno0no.hnn.request.admin.OrderSearchRequest;
import hanno0no.hnn.response.admin.AdminCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminCheckService {

    /*
    여기서 가지고 올 정보
    private final int orderId;          -> orders
    private final String fileName;      -> orders
    private final String teamNum;       -> orders
    private final String material;      -> material
    private final String State;         -> state
    private final String admin;         -> admin
     */

    private final OrdersRepository ordersRepository;
    private final MaterialRepository MaterialRepository;
    private final StateRepository stateRepository;
    private final AdminUserRepository adminUserRepository;
    private final EventInfoRepository eventInfoRepository;


    public List<AdminCheckResponse> getOrders(OrderSearchRequest orderSearchRequest) {

        Optional<EventInfo> activeEvent = eventInfoRepository.findByIsOpen();
        if (activeEvent.isEmpty()) {
            return List.of();
        }

        LocalDateTime start = activeEvent.get().getStartTime();
        LocalDateTime end = activeEvent.get().getEndTime();

        List<Orders> orders;

        String status = orderSearchRequest.getStatus();
        String manager = orderSearchRequest.getManager();

        if (StringUtils.hasText(status) && StringUtils.hasText(manager)) {
            int intStateId = stateRepository.findStateIdByState(orderSearchRequest.getStatus())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상태(status) 이름입니다: " + orderSearchRequest.getStatus()));

            orders = ordersRepository.findByAdminAndStateIdAndOrderedAtBetween(
                    orderSearchRequest.getManager(), intStateId, start, end);
        }
        else if (StringUtils.hasText(status)) {
            int intStateId = stateRepository.findStateIdByState(orderSearchRequest.getStatus())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상태(status) 이름입니다: " + orderSearchRequest.getStatus()));

            orders = ordersRepository.findOrdersByStateIdAndOrderedAtBetween(intStateId, start, end);
        }
        else if (StringUtils.hasText(manager)) {
            orders = ordersRepository.findByAdminAndOrderedAtBetween(
                    orderSearchRequest.getManager(), start, end);
        }
        else {
            orders = ordersRepository.findByOrderedAtBetween(start, end);
        }


        if (orders.isEmpty()) {
            return new ArrayList<>();
        }

        List<AdminCheckResponse> responses = new ArrayList<>();

        for (Orders order : orders) {
            int orderId = order.getOrderId();
            String fileName = order.getFileName();
            String teamNum = order.getTeam().getTeamNum();
            String material = order.getMaterial().getMaterialName();
            String state = order.getState().getState();
            String managerName = "";

            AdminUser admin = order.getAdmin();
            if (admin != null) {
                managerName = admin.getUserName();
            }


            responses.add(new AdminCheckResponse(orderId, fileName, teamNum, material, state, managerName));

        }

        return responses;
    }


}



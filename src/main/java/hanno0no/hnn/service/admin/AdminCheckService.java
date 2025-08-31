package hanno0no.hnn.service.admin;


import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.repository.material.MaterialRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import hanno0no.hnn.request.admin.AdminLoginRequest;
import hanno0no.hnn.request.admin.OrderSearchRequest;
import hanno0no.hnn.response.admin.AdminCheckResponse;
import hanno0no.hnn.response.admin.AdminLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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


    public List<AdminCheckResponse> getOrders(OrderSearchRequest orderSearchRequest) {

        List<Orders> orders = new ArrayList<>();

        String status = orderSearchRequest.getStatus();
        String manager = orderSearchRequest.getManager();

        if (StringUtils.hasText(status) && StringUtils.hasText(manager)) {
            int intStateId = stateRepository.findStateIdByState(orderSearchRequest.getStatus())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상태(status) 이름입니다: " + orderSearchRequest.getStatus()));

            orders = ordersRepository.findByAdminAndStateId(orderSearchRequest.getManager() ,intStateId);
        }
        else if (StringUtils.hasText(status)) {
            int intStateId = stateRepository.findStateIdByState(orderSearchRequest.getStatus())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상태(status) 이름입니다: " + orderSearchRequest.getStatus()));

            orders = ordersRepository.findOrdersByStateId(intStateId);
        }
        else if (StringUtils.hasText(manager)) {
            orders = ordersRepository.findByAdmin(orderSearchRequest.getManager());
        }
        else {
            orders = ordersRepository.findAll();
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



package hanno0no.hnn.service.checkStatus;


import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.domain.team.Team;
import hanno0no.hnn.repository.material.MaterialRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import hanno0no.hnn.repository.team.TeamRepository;
import hanno0no.hnn.response.checkStatus.CheckStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckStatusService {

    /*
    여기서 불러와야하는 정보
    1. 팀번호에 해당하는 주문 객체 전부
        주문번호 -> orders
        팀번호
        재질 -> metarial
        상태 -> state
        접수시간 -> orders
    */



    private final OrdersRepository ordersRepository;
    private final TeamRepository teamRepository;



    public List<CheckStatusResponse> getTeamOrderState(String teamNum) {

        Team team = teamRepository.findByTeamNum(teamNum)
                .orElseThrow(() -> new IllegalArgumentException("입력하신 팀 번호(" + teamNum + ")를 찾을 수 없습니다."));

        List<Orders> allOrders = ordersRepository.findByTeam(teamNum);

        if (allOrders.isEmpty()) {
            return Collections.emptyList(); // new ArrayList<>() 와 동일
        }

        List<CheckStatusResponse> responses = new ArrayList<>();

        for (Orders order : allOrders) {
            int orderId = order.getOrderId();
            String teamNumStr = order.getTeam().getTeamNum();
            String material = order.getMaterial().getMaterialName();
            String status = order.getState().getState();
            LocalDateTime orderTime = order.getOrderedAt();

            responses.add(new CheckStatusResponse(orderId, teamNumStr, material, status, orderTime));
        }
        return responses;

    }


}

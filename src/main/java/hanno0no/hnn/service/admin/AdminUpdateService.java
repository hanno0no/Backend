package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.domain.material.Material;
import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.domain.state.State;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.repository.material.MaterialRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import hanno0no.hnn.service.register.RegisterService;
import hanno0no.hnn.service.sse.SseEventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUpdateService {

    private final OrdersRepository ordersRepository;
    private final StateRepository stateRepository;
    private final AdminUserRepository adminUserRepository;
    private final MaterialRepository materialRepository;
    private final SseEventService sseEventService;

    @Transactional
    public void updateOrderStatus(Integer orderId, String newStatus) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. ID: " + orderId));

        State newState = stateRepository.findByState(newStatus)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상태입니다: " + newStatus));

        order.setState(newState);
        sseEventService.emit(SseEventService.INDEX_UPDATED);
        sseEventService.emit(SseEventService.ORDERS_UPDATED);
    }

    @Transactional
    public void updateOrderManager(Integer orderId, String newManagerName) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. ID: " + orderId));

        if (isUnassigned(newManagerName)) {
            order.setAdmin(null);
            sseEventService.emit(SseEventService.ORDERS_UPDATED);
            return;
        }

        AdminUser newManager = adminUserRepository.findByUserName(newManagerName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다: " + newManagerName));

        if (newManager.getDeletedAt() != null) {
            throw new IllegalArgumentException("삭제된 관리자에게는 담당자를 배정할 수 없습니다: " + newManagerName);
        }

        order.setAdmin(newManager);
        sseEventService.emit(SseEventService.ORDERS_UPDATED);
    }

    @Transactional
    public void updateOrderMaterial(Integer orderId, String newMaterialName) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. ID: " + orderId));

        Material newMaterial = materialRepository.findByMaterialName(newMaterialName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 재질입니다: " + newMaterialName));

        if (order.getMaterial().getMaterialNum() == newMaterial.getMaterialNum()) {
            return; // 재질이 그대로면 아무 것도 하지 않는다
        }

        // 재질이 바뀌면 레이저 컷팅기 설정(속도/강도)이 달라지므로, 기존 건은 실패 처리하고
        // 새 재질로 접수완료(디자인 진행 전) 상태의 주문을 새로 만든다. 담당자는 그대로 이어받는다.
        State acceptedState = stateRepository.findByState("accepted")
                .orElseThrow(() -> new IllegalStateException("accepted 상태를 찾을 수 없습니다."));
        State failedState = stateRepository.findByState("failed")
                .orElseThrow(() -> new IllegalStateException("failed 상태를 찾을 수 없습니다."));

        Orders newOrder = new Orders();
        newOrder.setTeam(order.getTeam());
        newOrder.setMaterial(newMaterial);
        newOrder.setState(acceptedState);
        newOrder.setAdmin(order.getAdmin());
        Orders savedNewOrder = ordersRepository.save(newOrder);

        String materialCode = RegisterService.getMaterialCode(newMaterial.getMaterialName());
        savedNewOrder.setFileName(String.format("%s_%d%s",
                savedNewOrder.getTeam().getTeamNum(), savedNewOrder.getOrderId(), materialCode));

        order.setState(failedState);

        sseEventService.emit(SseEventService.ORDERS_UPDATED);
    }

    @Transactional
    public void updateOrderHidden(Integer orderId, Boolean hidden) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. ID: " + orderId));

        boolean hide = hidden == null || hidden;
        order.setHiddenFromDashboard(hide);
        sseEventService.emit(SseEventService.INDEX_UPDATED);
    }

    private boolean isUnassigned(String managerName) {
        return managerName == null
                || managerName.isBlank()
                || "unassigned".equals(managerName.trim());
    }
}

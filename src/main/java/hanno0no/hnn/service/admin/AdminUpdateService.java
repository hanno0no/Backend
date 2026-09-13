package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.domain.material.Material;
import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.domain.state.State;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.repository.material.MaterialRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
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

        order.setAdmin(newManager);
        sseEventService.emit(SseEventService.ORDERS_UPDATED);
    }

    @Transactional
    public void updateOrderMaterial(Integer orderId, String newMaterialName) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. ID: " + orderId));

        Material newMaterial = materialRepository.findByMaterialName(newMaterialName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 재질입니다: " + newMaterialName));

        order.setMaterial(newMaterial);
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

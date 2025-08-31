package hanno0no.hnn.service.admin;


import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.domain.state.State;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUpdateService {

    private final OrdersRepository ordersRepository;
    private final StateRepository stateRepository;
    private final AdminUserRepository adminUserRepository;



    @Transactional
    public void updateOrderStatus(Integer orderId, String newStatus) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. ID: " + orderId));

        State newState = stateRepository.findByState(newStatus)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상태입니다: " + newStatus));

        order.setState(newState);

    }

    @Transactional
    public void updateOrderManager(Integer orderId, String newManagerName) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. ID: " + orderId));

        AdminUser newManager = adminUserRepository.findByUserName(newManagerName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다: " + newManagerName));

        order.setAdmin(newManager);

    }

}

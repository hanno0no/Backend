package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.exception.ForbiddenException;
import hanno0no.hnn.repository.adminuser.AdminUserRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import hanno0no.hnn.request.admin.AdminUserUpdateRequest;
import hanno0no.hnn.response.admin.AdminUserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminManagementService {

    private static final Set<String> ALLOWED_WORK_AREAS = Set.of("접수", "디자인", "출력", "3D프린트", "기타");
    // 종결 상태(완료/실패) - 이 상태의 주문은 관리자 삭제 시에도 담당자를 그대로 유지한다.
    private static final Set<String> TERMINAL_STATES = Set.of("print_complete", "failed", "picked_up");

    private final AdminUserRepository adminUserRepository;
    private final OrdersRepository ordersRepository;
    private final StateRepository stateRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AdminUserResponse> getAdmins() {
        List<AdminUserResponse> responses = new ArrayList<>();
        for (AdminUser admin : adminUserRepository.findAll()) {
            responses.add(new AdminUserResponse(
                    admin.getAdminId(),
                    admin.getUserName(),
                    new ArrayList<>(admin.getWorkAreas()),
                    admin.getPassword_hash() != null,
                    admin.getDeletedAt() != null
            ));
        }
        return responses;
    }

    @Transactional
    public void updateAdmin(int targetAdminId, AdminUserUpdateRequest request, AdminUser currentAdmin) {
        AdminUser target = adminUserRepository.findById(targetAdminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없습니다: " + targetAdminId));

        if (target.getDeletedAt() != null) {
            throw new IllegalStateException("삭제된 계정입니다. 먼저 재활성화해주세요.");
        }

        if (StringUtils.hasText(request.getPassword()) && targetAdminId != currentAdmin.getAdminId()) {
            throw new ForbiddenException("본인 계정만 비밀번호를 변경할 수 있습니다.");
        }

        if (StringUtils.hasText(request.getUserName()) && !request.getUserName().equals(target.getUserName())) {
            if (adminUserRepository.existsByUserName(request.getUserName())) {
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            }
            target.setUserName(request.getUserName());
        }

        if (StringUtils.hasText(request.getPassword())) {
            target.setPassword_hash(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getWorkAreas() != null) {
            for (String area : request.getWorkAreas()) {
                if (!ALLOWED_WORK_AREAS.contains(area)) {
                    throw new IllegalArgumentException("허용되지 않는 업무 담당 영역입니다: " + area);
                }
            }
            target.setWorkAreas(new HashSet<>(request.getWorkAreas()));
        }
    }

    @Transactional
    public void deleteAdmin(int targetAdminId, AdminUser currentAdmin) {
        if (targetAdminId == currentAdmin.getAdminId()) {
            throw new IllegalArgumentException("본인 계정은 삭제할 수 없습니다.");
        }
        AdminUser target = adminUserRepository.findById(targetAdminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없습니다: " + targetAdminId));
        if (target.getDeletedAt() != null) {
            throw new IllegalStateException("이미 삭제된 계정입니다.");
        }
        if (adminUserRepository.countByDeletedAtIsNull() <= 1) {
            throw new IllegalStateException("마지막 남은 관리자 계정은 삭제할 수 없습니다.");
        }

        // 완료/실패(종결) 주문은 담당자 이력을 그대로 남기고, 진행중인 주문만 미배정으로 되돌린다.
        List<Integer> terminalStateIds = new ArrayList<>();
        for (String stateName : TERMINAL_STATES) {
            stateRepository.findStateIdByState(stateName).ifPresent(terminalStateIds::add);
        }
        ordersRepository.unassignNonTerminalOrdersForAdmin(targetAdminId, terminalStateIds);

        // row 자체는 지우지 않는다 (완료/실패 주문의 admin_id FK가 이 row를 계속 참조하기 때문).
        // 로그인만 막고(deleted_at), 관리자 목록에서는 "삭제됨" 상태로 표시해 재활성화할 수 있게 한다.
        target.setDeletedAt(LocalDateTime.now());
    }

    @Transactional
    public void reactivateAdmin(int targetAdminId) {
        AdminUser target = adminUserRepository.findById(targetAdminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없습니다: " + targetAdminId));
        if (target.getDeletedAt() == null) {
            throw new IllegalStateException("이미 활성 상태인 계정입니다.");
        }
        target.setDeletedAt(null);
    }
}

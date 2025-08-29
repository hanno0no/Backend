package hanno0no.hnn.controller.admin;


import hanno0no.hnn.request.admin.*;
import hanno0no.hnn.response.admin.AdminCheckResponse;
import hanno0no.hnn.response.admin.AdminLoginResponse;
import hanno0no.hnn.service.admin.AdminCheckService;
import hanno0no.hnn.service.admin.AdminCreateService;
import hanno0no.hnn.service.admin.AdminLoginService;
import hanno0no.hnn.service.admin.AdminUpdateService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/hnn/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminLoginService adminLoginService;
    private final AdminCreateService adminCreateService;
    private final AdminCheckService adminCheckService;
    private final AdminUpdateService adminUpdateService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest request) {
        String token = adminLoginService.login(request);
        return ResponseEntity.ok(new AdminLoginResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<String>  createAdmin(@RequestBody AdminCreateRequest request) {

        int newAdminId = adminCreateService.createAdmin(request);

        return ResponseEntity.ok("관리자 계정이 성공적으로 생성되었습니다. ID: " + newAdminId);

    }

    @GetMapping("/view")
    public ResponseEntity<List<AdminCheckResponse>> getOrders(OrderSearchRequest orderSearchRequest) {

        List<AdminCheckResponse> responses = adminCheckService.getOrders(orderSearchRequest);

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Integer orderId, // 1. URL 경로에서 주문 ID를 추출
            @RequestBody OrderStatusUpdateRequest request // 2. 요청 본문에서 새로운 상태 정보를 추출
    ) {
        adminUpdateService.updateOrderStatus(orderId, request.getStatus());

        // 3. 성공적으로 수정되었음을 알리는 응답 (보통 본문 없음)
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{orderId}/manager")
    public ResponseEntity<Void> updateOrderManager( @PathVariable Integer orderId, @RequestBody OrderManagerUpdateRequest request) {

        adminUpdateService.updateOrderManager(orderId, request.getManager());

        return ResponseEntity.ok().build();
    }

}

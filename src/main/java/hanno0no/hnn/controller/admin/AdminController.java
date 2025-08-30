package hanno0no.hnn.controller.admin;


import hanno0no.hnn.request.admin.*;
import hanno0no.hnn.response.admin.AdminCheckResponse;
import hanno0no.hnn.response.admin.AdminLoginResponse;
import hanno0no.hnn.response.admin.AdminSettingResponse;
import hanno0no.hnn.service.admin.*;
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
    private final AdminSettingService adminSettingService;
    private final AdminSettingUpdateService adminSettingUpdateService;
    private final CreateSettingService createSettingService;

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

    @GetMapping("/setting")
    public ResponseEntity setting() {

        return ResponseEntity.ok(adminSettingService.getAdminSetting());
    }

    @PatchMapping("/setting")
    public ResponseEntity<Void> updateSetting(@RequestBody AdminSettingRequest request) {
        adminSettingUpdateService.updateAllSettings(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/create/material")
    public ResponseEntity<String> createMaterial(@RequestBody MaterialCreateRequest request) {
        String name = createSettingService.createMaterial(request);
        return ResponseEntity.ok(name+"재질이 성공적으로 추가되었습니다.");
    }

    @PostMapping("/create/message")
    public ResponseEntity<String> createMessage(@RequestBody MessageCreateRequest request) {
        Integer messageId = createSettingService.createMessage(request);
        return ResponseEntity.ok("메시지가 성공적으로 추가되었습니다. ID:" +messageId);
    }

    @PostMapping("/create/eventinfo")
    public ResponseEntity<String> createEventInfo(@RequestBody EventInfoCreateRequest request) {
        String eventname = createSettingService.createEventInfo(request);
        return ResponseEntity.ok(eventname + "대회가 성공적으로 추가되었습니다.");
    }

}

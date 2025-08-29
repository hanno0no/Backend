package hanno0no.hnn.controller.admin;


import hanno0no.hnn.request.admin.AdminCreateRequest;
import hanno0no.hnn.request.admin.AdminLoginRequest;
import hanno0no.hnn.request.admin.OrderSearchRequest;
import hanno0no.hnn.response.admin.AdminCheckResponse;
import hanno0no.hnn.response.admin.AdminLoginResponse;
import hanno0no.hnn.service.admin.AdminCheckService;
import hanno0no.hnn.service.admin.AdminCreateService;
import hanno0no.hnn.service.admin.AdminLoginService;
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

}

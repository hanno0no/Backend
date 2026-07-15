package hanno0no.hnn.controller.register;

import hanno0no.hnn.request.register.RegisterRequest;
import hanno0no.hnn.response.register.RegisterResponse;
import hanno0no.hnn.service.register.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// /registar 는 프론트 전환 기간 동안 병행 지원 (Phase 0-4)
@RequestMapping({"/hnn/register", "/hnn/registar"})
@RequiredArgsConstructor
@RestController
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping
    public ResponseEntity<RegisterResponse> createOrder(@RequestBody RegisterRequest request) {
        int newOrderId = registerService.createOrder(request);
        return ResponseEntity.ok(new RegisterResponse(newOrderId, "접수가 완료되었습니다."));
    }

    @GetMapping("/getmaterial")
    public ResponseEntity<List<String>> getMaterial() {
        return ResponseEntity.ok(registerService.getMaterialNames());
    }

    @GetMapping("/getstate")
    public ResponseEntity<List<String>> getState() {
        return ResponseEntity.ok(registerService.getStateNames());
    }

    @GetMapping("/getadminname")
    public ResponseEntity<List<String>> getAdminName() {
        return ResponseEntity.ok(registerService.getAdminNames());
    }
}

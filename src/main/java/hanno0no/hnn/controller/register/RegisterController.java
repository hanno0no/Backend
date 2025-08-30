package hanno0no.hnn.controller.register;

import hanno0no.hnn.request.register.RegisterRequest;
import hanno0no.hnn.service.register.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@CrossOrigin(origins = "*")
@RequestMapping("/hnn/registar")
@RequiredArgsConstructor
@RestController
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping()
    public ResponseEntity<String> createOrder(@RequestBody RegisterRequest request) {
        int newOrderId = registerService.createOrder(request);

        return ResponseEntity.ok("접수가 완료되었습니다. 접수번호: " + newOrderId);
    }

    @GetMapping("/getmaterial")
    public ResponseEntity<List<String>> getMaterial() {
        List<String> responseList = registerService.getMaterialNames();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/getstate")
    public ResponseEntity<List<String>> getState() {
        List<String> responseList = registerService.getStateNames();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/getadminname")
    public ResponseEntity<List<String>> getAdminName() {
        List<String> responseList = registerService.getAdminNames();
        return ResponseEntity.ok(responseList);
    }

}

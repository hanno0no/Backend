package hanno0no.hnn.controller.register;

import hanno0no.hnn.request.register.RegisterRequest;
import hanno0no.hnn.service.register.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RequestMapping("/hnn")
@RequiredArgsConstructor
@RestController
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/registar")
    public ResponseEntity<String> createOrder(@RequestBody RegisterRequest request) {
        int newOrderId = registerService.createOrder(request);

        return ResponseEntity.ok("접수가 완료되었습니다. 접수번호: " + newOrderId);
    }

}

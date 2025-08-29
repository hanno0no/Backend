package hanno0no.hnn.controller.CheckStatus;


import hanno0no.hnn.response.checkStatus.CheckStatusResponse;
import hanno0no.hnn.service.checkStatus.CheckStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/hnn/checkStatus")
@RequiredArgsConstructor
public class checkStatusController {

    private final CheckStatusService checkStatusService;

    @GetMapping("/view")                // 리엑트로 프론트를 구현하면 사실상 필요없는 코드
    public String checkStatusPage() {
        // `resources/templates` 폴더 안에 있는 `checkStatus.html` 파일을 찾아서
        // 사용자에게 보여줍니다.
        return "checkStatus";
    }

    @GetMapping
    public ResponseEntity<List<CheckStatusResponse>> getStatusByTeamName(@RequestParam String teamNum) {

        List<CheckStatusResponse> responseList = checkStatusService.getTeamOrderState(teamNum);

        return ResponseEntity.ok(responseList);
    }

}

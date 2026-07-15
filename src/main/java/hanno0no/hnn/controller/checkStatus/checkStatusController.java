package hanno0no.hnn.controller.checkStatus;

import hanno0no.hnn.response.checkStatus.CheckStatusResponse;
import hanno0no.hnn.service.checkStatus.CheckStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hnn/checkStatus")
@RequiredArgsConstructor
public class checkStatusController {

    private final CheckStatusService checkStatusService;

    @GetMapping
    public ResponseEntity<List<CheckStatusResponse>> getStatusByTeamName(@RequestParam String teamNum) {
        return ResponseEntity.ok(checkStatusService.getTeamOrderState(teamNum));
    }
}

package hanno0no.hnn.controller.index;


import hanno0no.hnn.response.index.IndexStatusResponse;
import hanno0no.hnn.service.index.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/hnn/index")
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;

    @GetMapping
    public ResponseEntity index() {

        IndexStatusResponse responseBody = indexService.getIndexInfo();
//        System.out.println("test");
        return ResponseEntity.ok(responseBody);
    }


}

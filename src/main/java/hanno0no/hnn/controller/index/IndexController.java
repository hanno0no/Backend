package hanno0no.hnn.controller.index;


import hanno0no.hnn.service.index.IndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hnn")
public class IndexController {

    @GetMapping("/index")
    public String index() {

        return new IndexService().getIndexInfo();
    }


}

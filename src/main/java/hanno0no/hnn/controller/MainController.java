package hanno0no.hnn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hnn")
public class MainController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }


}


package hanno0no.hnn.request.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminCreateRequest {

    private String userName;
    private List<String> workAreas;

}

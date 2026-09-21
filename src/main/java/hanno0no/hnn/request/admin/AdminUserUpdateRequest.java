package hanno0no.hnn.request.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminUserUpdateRequest {

    private String userName;
    private String password;
    private List<String> workAreas;

}

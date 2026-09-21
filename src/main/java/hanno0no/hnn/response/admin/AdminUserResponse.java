package hanno0no.hnn.response.admin;

import lombok.Getter;

import java.util.List;

@Getter
public class AdminUserResponse {

    private final int adminId;
    private final String userName;
    private final List<String> workAreas;
    private final boolean passwordSet;

    public AdminUserResponse(int adminId, String userName, List<String> workAreas, boolean passwordSet) {
        this.adminId = adminId;
        this.userName = userName;
        this.workAreas = workAreas;
        this.passwordSet = passwordSet;
    }
}

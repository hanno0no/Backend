package hanno0no.hnn.response.admin;

import lombok.Getter;

@Getter
public class TeamResponse {

    private final String teamNum;
    private final String phoneNumber;

    public TeamResponse(String teamNum, String phoneNumber) {
        this.teamNum = teamNum;
        this.phoneNumber = phoneNumber;
    }
}

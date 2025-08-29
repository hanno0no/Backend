package hanno0no.hnn.response.admin;


import lombok.Getter;

@Getter
public class AdminLoginResponse {

    private final String accessToken;

    public AdminLoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }


}

package hanno0no.hnn.request.admin;

import lombok.Getter;

@Getter
public class AdminCreateRequest {

    private String userName;
    private String password;    // 클라이언트로부터는 암호화되지 않은 원본 비밀번호를 받습니다.
    private String role;

}

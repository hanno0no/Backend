package hanno0no.hnn.response.register;

import lombok.Getter;

@Getter
public class RegisterResponse {

    private final int orderId;
    private final String message;

    public RegisterResponse(int orderId, String message) {
        this.orderId = orderId;
        this.message = message;
    }
}

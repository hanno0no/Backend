package hanno0no.hnn.response.checkStatus;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CheckStatusResponse {

    private final int orderId;
    private final String teamNum;
    private final String material;
    private final String status;
    private final LocalDateTime orderTime;


    public CheckStatusResponse(int orderId, String teamNum, String material, String status, LocalDateTime orderTime) {
        this.orderId = orderId;
        this.teamNum = teamNum;
        this.material = material;
        this.status = status;
        this.orderTime = orderTime;
    }


}

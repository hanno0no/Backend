package hanno0no.hnn.response.admin;

public class AdminCheckResponse {

    private final int orderId;
    private final String fileName;
    private final String teamNum;
    private final String material;
    private final String state;
    private final String admin;     // Manager

    public AdminCheckResponse(int orderId, String fileName, String teamNum, String material, String State, String admin) {
        this.orderId = orderId;
        this.fileName = fileName;
        this.teamNum = teamNum;
        this.material = material;
        this.state = State;
        this.admin = admin;
    }


}

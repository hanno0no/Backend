package hanno0no.hnn.response.index;

import java.time.LocalDateTime;
import java.util.List;

public class IndexStatusResponse {

    private List<String> completedTeam;
    private List<String> waitingTeam;
    private LocalDateTime endTime;
    private List<String> emergencyMessage;
    private List<String> messages;


    // 생성자
    public IndexStatusResponse(List<String> completedTeam, List<String> waitingTeam, LocalDateTime endTime, List<String> emergencyMessage, List<String> messages) {
        this.completedTeam = completedTeam;
        this.waitingTeam = waitingTeam;
        this.endTime = endTime;
        this.emergencyMessage = emergencyMessage;
        this.messages = messages;
    }


    public List<String> getCompletedTeam() {
        return completedTeam;
    }
    public List<String> getWaitingTeam() {
        return waitingTeam;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public List<String> getEmergencyMessage() {
        return emergencyMessage;
    }
    public List<String> getMessages() {
        return messages;
    }

}




package hanno0no.hnn.response.admin;

import hanno0no.hnn.domain.eventinfo.EventInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventInfoDto {
    private int eventId;
    private String eventName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isOpen;
    private String description;
    private Integer completedLimit;
    private Integer waitingLimit;

    public EventInfoDto(EventInfo eventInfo) {
        this.eventId = eventInfo.getEventId();
        this.eventName = eventInfo.getEventName();
        this.startTime = eventInfo.getStartTime();
        this.endTime = eventInfo.getEndTime();
        this.isOpen = eventInfo.isOpen();
        this.description = eventInfo.getDescription();
        this.completedLimit = eventInfo.getCompletedLimit();
        this.waitingLimit = eventInfo.getWaitingLimit();
    }

}

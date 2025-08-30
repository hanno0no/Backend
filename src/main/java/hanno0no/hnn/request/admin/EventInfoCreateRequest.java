package hanno0no.hnn.request.admin;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class EventInfoCreateRequest {

    private String eventName;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean Open;

}

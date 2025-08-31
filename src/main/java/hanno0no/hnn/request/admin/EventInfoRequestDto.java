package hanno0no.hnn.request.admin;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EventInfoRequestDto {
    private int eventId;
    private String eventName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isOpen;
    private String description;
}

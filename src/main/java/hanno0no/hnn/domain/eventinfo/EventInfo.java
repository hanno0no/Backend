package hanno0no.hnn.domain.eventinfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_info")
@Getter
@Setter
public class EventInfo {

    @Id
    @Column(name="event_id")
    private int eventId;

    @Column(name = "name")
    private String eventName;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "is_open")
    private boolean isOpen;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
}

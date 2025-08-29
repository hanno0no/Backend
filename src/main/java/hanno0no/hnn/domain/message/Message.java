package hanno0no.hnn.domain.message;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
public class Message {

    @Id
    @Column(name = "message_id")
    private int messageId;

    @Column(name = "content")
    private String content;

    @Column(name = "is_emergency")
    private boolean isEmergency;

    @Column(name = "is_display")
    private boolean isDisplay;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


}

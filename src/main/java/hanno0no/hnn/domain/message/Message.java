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
    private boolean emergency;

    @Column(name = "is_display")
    private boolean display;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;


}

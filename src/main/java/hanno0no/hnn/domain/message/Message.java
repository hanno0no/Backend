package hanno0no.hnn.domain.message;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @Column(name = "message_id")
    private String messageId;

    @Column(name = "content")
    private String content;

    @Column(name = "is_emergency")
    private boolean isEmergency;

    @Column(name = "is_display")
    private boolean isDisplay;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public String getMessageId() {
        return messageId;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEmergency() {
        return isEmergency;
    }
    public void setEmergency(boolean emergency) {
        isEmergency = emergency;
    }

    public boolean isDisplay() {
        return isDisplay;
    }
    public void setDisplay(boolean display) {
        isDisplay = display;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

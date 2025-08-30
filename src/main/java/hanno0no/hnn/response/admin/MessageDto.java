package hanno0no.hnn.response.admin;

import hanno0no.hnn.domain.message.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private int messageId;
    private String content;
    private boolean isEmergency;
    private boolean isDisplay;

    public MessageDto(Message message) {
        this.messageId = message.getMessageId();
        this.content = message.getContent();
        this.isEmergency = message.isEmergency();
        this.isDisplay = message.isDisplay();
    }

}

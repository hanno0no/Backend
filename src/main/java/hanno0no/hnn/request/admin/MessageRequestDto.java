package hanno0no.hnn.request.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageRequestDto {
    private int messageId;
    private String content;
    private Boolean isEmergency;
    private Boolean isDisplay;
}

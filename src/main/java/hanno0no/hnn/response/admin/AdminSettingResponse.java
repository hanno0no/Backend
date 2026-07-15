package hanno0no.hnn.response.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AdminSettingResponse {

    private final List<EventInfoDto> eventInfos;
    private final List<MessageDto> messages;
    private final List<MaterialDto> materials;
    private final EventInfoDto openEventInfo;
    private final Integer completedLimit;
    private final Integer waitingLimit;
}

package hanno0no.hnn.request.admin;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminSettingRequest {
    private List<EventInfoRequestDto> eventInfoRequestDtos;
    private List<MessageRequestDto> messageRequestDtos;
    private List<MaterialRequestDto> materialRequestDtos;

}

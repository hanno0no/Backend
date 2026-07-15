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
    /** 활성 이벤트의 대시보드 완료 명단 건수 */
    private Integer completedLimit;
    /** 활성 이벤트의 대시보드 대기 명단 건수 */
    private Integer waitingLimit;

}

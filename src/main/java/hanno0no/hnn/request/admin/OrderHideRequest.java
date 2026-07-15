package hanno0no.hnn.request.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderHideRequest {
    /** true면 완료 명단에서 숨김, false면 다시 표시. null이면 true로 처리 */
    private Boolean hidden;
}

package hanno0no.hnn.request.admin;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderSearchRequest {
    // 복수 선택 가능: ?status=a&status=b 형태의 반복 쿼리 파라미터를 그대로 바인딩.
    private List<String> status;
    private String manager;
    private String material;
    private String teamNum;
}

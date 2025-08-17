package hanno0no.hnn.service.index;


import hanno0no.hnn.repository.eventinfo.EventInofoRepository;
import hanno0no.hnn.repository.message.MessageRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import hanno0no.hnn.response.index.IndexStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexService {

    /*
    여기서 불러와야하는 정보
    1. 완료된 팀 번호 -> 리스트          // orders 테이블에서 조회해야함           // 이거 정렬은 가장 최근에 완료된 순서로 정렬해야할듯
    2. 진행중인 팀 번호 -> 리스트         // orders 테이블에서 조회해야함
    3. 끝나는 시간                       // event_info 테이블에서 조회
    4. 중요 공지                        // message 테이블에서 조회
    5. 일반 공지   -> 리스트               // message 테이블에서 조회
     */

    private final OrdersRepository ordersRepository;
    private final EventInofoRepository eventInofoRepository;
    private final MessageRepository messageRepository;


    public IndexStatusResponse getIndexInfo() {



        return null;
    }


}

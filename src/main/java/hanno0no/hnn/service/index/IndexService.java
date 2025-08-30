package hanno0no.hnn.service.index;


import hanno0no.hnn.domain.eventinfo.EventInfo;
import hanno0no.hnn.domain.message.Message;
import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.repository.eventinfo.EventInfoRepository;
import hanno0no.hnn.repository.message.MessageRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import hanno0no.hnn.response.index.IndexStatusResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final EventInfoRepository eventInfoRepository;
    private final MessageRepository messageRepository;
    private final StateRepository stateRepository;


    public IndexStatusResponse getIndexInfo() {

        Integer completeStateId = stateRepository.findStateIdByState("print")   // 프린트 완료 = 최종 완료
                .orElseThrow(() -> new EntityNotFoundException("'print' 상태의 ID를 찾을 수 없습니다."));
        Integer rejectionStateId = stateRepository.findStateIdByState("rejection")
                .orElseThrow(() -> new EntityNotFoundException("'rejection' 상태의 ID를 찾을 수 없습니다."));

        List<Orders> completeTeams = ordersRepository.findOrdersByStateId(completeStateId);

//        List<String> completeTeamNum = completeTeams.stream().map(order -> order.getTeam().getTeamNum()).collect(Collectors.toList());
//        Collections.reverse(completeTeamNum);

        List<String> completeTeamNum = completeTeams.stream()
                .map(order -> order.getTeam().getTeamNum() + "_" + order.getOrderId())
                .collect(Collectors.toList());
        Collections.reverse(completeTeamNum);


        List<Integer> excludedStateIds = Arrays.asList(completeStateId, rejectionStateId);
        List<Orders> ongoingTeams = ordersRepository.findByState_StateIdNotIn(excludedStateIds);

//        List<String> ongoingTeamNum = ongoingTeams.stream().map(order -> order.getTeam().getTeamNum()).collect(Collectors.toList());
//        Collections.reverse(ongoingTeamNum);

        List<String> ongoingTeamNum = ongoingTeams.stream()
                .map(order -> order.getTeam().getTeamNum() + "_" + order.getOrderId())
                .collect(Collectors.toList());
        Collections.reverse(ongoingTeamNum);


        EventInfo activeEvent = eventInfoRepository.findByIsOpen()
                .orElseThrow(() -> new EntityNotFoundException("현재 진행중인 이벤트가 없습니다."));
        Integer eventId = activeEvent.getEventId();


        LocalDateTime endTime = eventInfoRepository.findEndTimeByEventId(eventId)
                .orElseThrow(() -> new EntityNotFoundException("해당 이벤트를 찾을 수 없습니다."));

        List<Message> emergencyMessage = messageRepository.findAllByEmergency();

        List<String> emergencyMessageContent = emergencyMessage.stream().map(message -> message.getContent()).collect(Collectors.toList());
        Collections.reverse(emergencyMessageContent);

        List<Message> generalMessage = messageRepository.findAllByNonEmergency();
        List<String> generalMessageContent = generalMessage.stream().map(message -> message.getContent()).collect(Collectors.toList());
        Collections.reverse(generalMessageContent);

        IndexStatusResponse response = new IndexStatusResponse(
                completeTeamNum, ongoingTeamNum, endTime, emergencyMessageContent, generalMessageContent
        );


        return response;


    }


}

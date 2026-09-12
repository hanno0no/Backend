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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IndexService {

    private static final int DEFAULT_COMPLETED_LIMIT = 9;
    private static final int DEFAULT_WAITING_LIMIT = 12;

    private final OrdersRepository ordersRepository;
    private final EventInfoRepository eventInfoRepository;
    private final MessageRepository messageRepository;
    private final StateRepository stateRepository;

    public IndexStatusResponse getIndexInfo() {
        EventInfo activeEvent = eventInfoRepository.findByIsOpen()
                .orElseThrow(() -> new EntityNotFoundException("현재 진행중인 이벤트가 없습니다."));

        int completedLimit = resolveLimit(activeEvent.getCompletedLimit(), DEFAULT_COMPLETED_LIMIT);
        int waitingLimit = resolveLimit(activeEvent.getWaitingLimit(), DEFAULT_WAITING_LIMIT);

        Integer completeStateId = stateRepository.findStateIdByState("print_complete")
                .orElseThrow(() -> new EntityNotFoundException("'print_complete' 상태의 ID를 찾을 수 없습니다."));

        Integer acceptedStateId = stateRepository.findStateIdByState("accepted")
                .orElseThrow(() -> new EntityNotFoundException("'accepted' 상태의 ID를 찾을 수 없습니다."));
        Integer designCompleteStateId = stateRepository.findStateIdByState("design_complete")
                .orElseThrow(() -> new EntityNotFoundException("'design_complete' 상태의 ID를 찾을 수 없습니다."));

        LocalDateTime start = activeEvent.getStartTime();
        LocalDateTime end = activeEvent.getEndTime();
        if (end == null) {
            end = eventInfoRepository.findEndTimeByEventId(activeEvent.getEventId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 이벤트를 찾을 수 없습니다."));
        }

        List<Orders> completeTeams = ordersRepository.findTopCompletedOrders(
                completeStateId, start, end, PageRequest.of(0, completedLimit));

        // findTopCompletedOrders가 이미 updatedAt DESC로 정렬해 반환하므로
        // 가장 최근에 완료된 팀이 배열 맨 앞에 오도록 별도로 뒤집지 않는다.
        List<String> completeTeamNum = completeTeams.stream()
                .map(order -> order.getTeam().getTeamNum() + "_" + order.getOrderId())
                .collect(Collectors.toList());

        List<Integer> waitingStateIds = Arrays.asList(acceptedStateId, designCompleteStateId);
        List<Orders> ongoingTeams = ordersRepository.findOldestWaitingOrders(
                waitingStateIds, start, end, PageRequest.of(0, waitingLimit));

        List<String> ongoingTeamNum = ongoingTeams.stream()
                .map(order -> order.getTeam().getTeamNum() + "_" + order.getOrderId())
                .collect(Collectors.toList());

        List<Message> emergencyMessage = messageRepository.findAllByEmergency();
        List<String> emergencyMessageContent = emergencyMessage.stream()
                .map(Message::getContent)
                .collect(Collectors.toList());
        Collections.reverse(emergencyMessageContent);

        List<Message> generalMessage = messageRepository.findAllByNonEmergency();
        List<String> generalMessageContent = generalMessage.stream()
                .map(Message::getContent)
                .collect(Collectors.toList());
        Collections.reverse(generalMessageContent);

        return new IndexStatusResponse(
                completeTeamNum, ongoingTeamNum, end, emergencyMessageContent, generalMessageContent
        );
    }

    private int resolveLimit(Integer configured, int defaultValue) {
        if (configured == null || configured <= 0) {
            return defaultValue;
        }
        return configured;
    }
}

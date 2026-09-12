package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.eventinfo.EventInfo;
import hanno0no.hnn.domain.state.State;
import hanno0no.hnn.repository.eventinfo.EventInfoRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final OrdersRepository ordersRepository;
    private final StateRepository stateRepository;
    private final EventInfoRepository eventInfoRepository;

    public Map<String, Long> getStats() {
        EventInfo activeEvent = eventInfoRepository.findByIsOpen()
                .orElseThrow(() -> new EntityNotFoundException("현재 진행중인 이벤트가 없습니다."));

        LocalDateTime start = activeEvent.getStartTime();
        LocalDateTime end = activeEvent.getEndTime();
        if (end == null) {
            end = eventInfoRepository.findEndTimeByEventId(activeEvent.getEventId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 이벤트를 찾을 수 없습니다."));
        }

        Map<String, Long> stats = new LinkedHashMap<>();
        for (State state : stateRepository.findAll()) {
            long count = ordersRepository.countByStateIdAndOrderedAtBetween(state.getStateId(), start, end);
            stats.put(state.getState(), count);
        }
        return stats;
    }
}

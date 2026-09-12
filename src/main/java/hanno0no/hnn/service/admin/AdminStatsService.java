package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.eventinfo.EventInfo;
import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.domain.state.State;
import hanno0no.hnn.repository.eventinfo.EventInfoRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final OrdersRepository ordersRepository;
    private final StateRepository stateRepository;
    private final EventInfoRepository eventInfoRepository;

    public Map<String, Object> getStats() {
        EventInfo activeEvent = eventInfoRepository.findByIsOpen()
                .orElseThrow(() -> new EntityNotFoundException("현재 진행중인 이벤트가 없습니다."));

        LocalDateTime start = activeEvent.getStartTime();
        LocalDateTime end = activeEvent.getEndTime();
        if (end == null) {
            end = eventInfoRepository.findEndTimeByEventId(activeEvent.getEventId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 이벤트를 찾을 수 없습니다."));
        }

        List<State> states = stateRepository.findAll();

        Map<String, Long> byState = new LinkedHashMap<>();
        for (State state : states) {
            long count = ordersRepository.countByStateIdAndOrderedAtBetween(state.getStateId(), start, end);
            byState.put(state.getState(), count);
        }

        List<Orders> orders = ordersRepository.findByOrderedAtBetween(start, end);

        // 이벤트 기간이 길면(48시간 초과) 포인트 수 폭증을 막기 위해 일 단위로 집계한다.
        boolean useDailyBuckets = ChronoUnit.HOURS.between(
                start.truncatedTo(ChronoUnit.HOURS), end.truncatedTo(ChronoUnit.HOURS)) > 48;
        ChronoUnit unit = useDailyBuckets ? ChronoUnit.DAYS : ChronoUnit.HOURS;

        List<String> stateNames = states.stream().map(State::getState).collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("byState", byState);
        response.put("timelineGranularity", useDailyBuckets ? "day" : "hour");
        response.put("timeline", buildTimeline(orders, start, end, unit, stateNames));
        return response;
    }

    /**
     * 상태별로 "그 시간대에 해당 상태로 바뀐" 주문 건수와, 접수 시각(orderedAt) 기준
     * "그 시간대에 새로 접수된" 건수(registered)를 함께 집계한다.
     * updatedAt이 없는(과거 데이터) 주문은 접수 시각(orderedAt)을 대신 사용한다.
     *
     * 상태별 건수는 "그 순간의 스냅샷"이라 접수/디자인완료처럼 스쳐 지나가는 상태는
     * 누적으로 보기 어렵다. 반면 registered는 orderedAt이 절대 바뀌지 않고,
     * picked_up·failed는 한번 도달하면 상태가 바뀌지 않는 종결 상태라
     * 프론트에서 누적합을 구해도 항상 정확하다.
     */
    private List<Map<String, Object>> buildTimeline(List<Orders> orders, LocalDateTime start, LocalDateTime end,
                                                     ChronoUnit unit, List<String> stateNames) {
        LocalDateTime bucketStart = start.truncatedTo(unit);
        LocalDateTime bucketEnd = end.truncatedTo(unit);

        Map<LocalDateTime, Map<String, Long>> buckets = new LinkedHashMap<>();
        for (LocalDateTime t = bucketStart; !t.isAfter(bucketEnd); t = t.plus(1, unit)) {
            Map<String, Long> counts = new LinkedHashMap<>();
            counts.put("registered", 0L);
            stateNames.forEach(name -> counts.put(name, 0L));
            buckets.put(t, counts);
        }

        for (Orders order : orders) {
            Map<String, Long> registeredBucket = buckets.get(order.getOrderedAt().truncatedTo(unit));
            if (registeredBucket != null) {
                registeredBucket.merge("registered", 1L, Long::sum);
            }

            LocalDateTime timestamp = order.getUpdatedAt() != null ? order.getUpdatedAt() : order.getOrderedAt();
            Map<String, Long> stateBucket = buckets.get(timestamp.truncatedTo(unit));
            if (stateBucket != null) {
                stateBucket.merge(order.getState().getState(), 1L, Long::sum);
            }
        }

        List<Map<String, Object>> timeline = new ArrayList<>();
        for (Map.Entry<LocalDateTime, Map<String, Long>> entry : buckets.entrySet()) {
            Map<String, Object> point = new LinkedHashMap<>();
            point.put("hour", entry.getKey());
            point.putAll(entry.getValue());
            timeline.add(point);
        }
        return timeline;
    }
}

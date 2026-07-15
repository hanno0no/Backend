package hanno0no.hnn.domain.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * 기획서 §5.3 허용 상태 전이만 통과시킨다.
 */
@Component
public class StateTransitionValidator {

    private static final Map<String, Set<String>> ALLOWED_TRANSITIONS = Map.of(
            "submitted", Set.of("accepted", "failed"),
            "accepted", Set.of("design_complete", "failed"),
            "design_complete", Set.of("print_complete", "failed"),
            "print_complete", Set.of("picked_up", "failed"),
            "picked_up", Set.of(),
            "failed", Set.of()
    );

    public void validate(String from, String to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("상태 값이 비어 있습니다.");
        }
        if (from.equals(to)) {
            return;
        }

        Set<String> allowed = ALLOWED_TRANSITIONS.get(from);
        if (allowed == null || !allowed.contains(to)) {
            throw new IllegalArgumentException(
                    "허용되지 않는 상태 전이입니다: " + from + " → " + to);
        }
    }
}

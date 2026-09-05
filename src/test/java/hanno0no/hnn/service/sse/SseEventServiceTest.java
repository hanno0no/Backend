package hanno0no.hnn.service.sse;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SseEventServiceTest {

    @Test
    void broadcastIndexUpdatedToLiveEmitterIncludesEventType() {
        SseEventService service = new SseEventService();
        SseEmitter emitter = service.subscribe();

        assertNotNull(emitter);
        assertDoesNotThrow(() -> service.emit(SseEventService.INDEX_UPDATED));

        Object earlySendAttempts = ReflectionTestUtils.getField(emitter, "earlySendAttempts");
        assertTrue(earlySendAttempts instanceof Collection<?> events && events.stream()
                .map(event -> ReflectionTestUtils.getField(event, "data"))
                .map(data -> Objects.toString(data, ""))
                .anyMatch(data -> data.contains("\"type\":\"index_updated\"")));
    }
}

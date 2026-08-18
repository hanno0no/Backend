package hanno0no.hnn.service.sse;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SseEventServiceTest {

    @Test
    void broadcastIndexUpdatedToLiveEmitter() {
        SseEventService service = new SseEventService();
        SseEmitter emitter = service.subscribe();

        assertNotNull(emitter);
        assertDoesNotThrow(() -> service.emit(SseEventService.INDEX_UPDATED));
    }
}

package hanno0no.hnn.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void passwordNotSetExceptionMapsTo428() {
        ResponseEntity<?> response = handler.handlePasswordNotSetException(
                new PasswordNotSetException("비밀번호를 먼저 설정해주세요."));

        assertEquals(HttpStatus.PRECONDITION_REQUIRED, response.getStatusCode());
    }

    @Test
    void forbiddenExceptionMapsTo403() {
        ResponseEntity<?> response = handler.handleForbiddenException(
                new ForbiddenException("본인 계정만 비밀번호를 변경할 수 있습니다."));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}

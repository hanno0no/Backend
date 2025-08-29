package hanno0no.hnn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 2. @RestControllerAdvice 어노테이션을 클래스에 붙여줍니다.
//    이것으로 이 클래스가 모든 @RestController의 예외를 담당한다는 것을 스프링에게 알립니다.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 3. @ExceptionHandler 어노테이션을 사용해 어떤 예외를 처리할지 지정합니다.
    //    여기서는 서비스 로직에서 던진 IllegalArgumentException이 발생하면 이 메소드가 호출됩니다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {

        // 4. 예외가 발생했을 때, 클라이언트에게 보낼 HTTP 응답을 직접 만듭니다.
        //    - HTTP 상태는 404 Not Found 로 설정합니다.
        //    - 응답 본문(Body)에는 서비스에서 던진 예외 메시지(e.getMessage())를 담아줍니다.
        //      예: "입력하신 팀 번호(T3)를 찾을 수 없습니다."
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // 필요하다면 다른 종류의 예외를 처리하는 핸들러를 여기에 계속 추가할 수 있습니다.
    // 예를 들어, NullPointerException에 대해 500 에러를 반환하고 싶다면:
    /*
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부에서 오류가 발생했습니다.");
    }
    */
}
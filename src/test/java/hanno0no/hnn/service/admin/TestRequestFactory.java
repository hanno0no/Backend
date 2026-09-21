package hanno0no.hnn.service.admin;

import hanno0no.hnn.request.admin.AdminLoginRequest;

import java.lang.reflect.Field;

/** 테스트 전용: setter가 없는 요청 DTO에 값을 채워 넣기 위한 헬퍼. */
final class TestRequestFactory {

    private TestRequestFactory() {}

    static AdminLoginRequest loginRequest(String username, String password) {
        AdminLoginRequest request = new AdminLoginRequest();
        setField(request, "username", username);
        setField(request, "password", password);
        return request;
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}

package hanno0no.hnn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 1. 모든 경로에 대해
                .allowedOrigins("http://localhost:5173",
                                "http://localhost:8080",
                                "https://hanno0no.vercel.app",
                                "http://hanno0no.vercel.app"
                ) // 2. 이 출처의 요청을 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // 3. 허용할 HTTP 메서드
                .allowedHeaders("Authorization", "Content-Type") // 4. 허용할 헤더 (Authorization 추가가 핵심!)
                .allowCredentials(true) // 5. 인증 정보 허용
                .maxAge(3600); // 6. Preflight 요청 결과를 캐시할 시간(초)
    }
}

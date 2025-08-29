package hanno0no.hnn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호를 안전하게 암호화하기 위한 BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 비활성화 (JWT 사용 시 불필요)
                .csrf(csrf -> csrf.disable())

                // 세션을 사용하지 않도록 설정 (Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // HTTP 요청에 대한 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // '/api/v1/admin/login' 경로는 누구나 접근 허용
                        .requestMatchers("/api/v1/admin/login").permitAll()
                        // 그 외의 모든 요청은 일단 허용 (나중에 JWT 필터로 개별 검증)
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
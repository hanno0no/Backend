package hanno0no.hnn.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호를 안전하게 암호화하기 위한 BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // '/api/v1/admin/login' 과 '/api/v1/admins/signup'은 누구나 접근 가능
                        .requestMatchers("/api/v1/admin/login", "/api/v1/admins/signup").permitAll()

                        // '/hnn/admin/**' 경로는 'ADMIN' 역할을 가진 사용자만 접근 가능
                        .requestMatchers("/hnn/admin/**").hasRole("ADMIN")

                        // 그 외의 다른 모든 요청은 일단 인증만 되면 접근 가능하도록 설정 (필요에 따라 수정)
                        .anyRequest().authenticated()
                )
                // ✨ Spring Security의 기본 필터 앞에 우리가 만든 JWT 필터를 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
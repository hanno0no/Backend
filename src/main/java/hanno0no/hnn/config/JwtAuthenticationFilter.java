package hanno0no.hnn.config;

import hanno0no.hnn.service.admin.AdminUserDetailsService; // UserDetailsService 구현체
import hanno0no.hnn.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AdminUserDetailsService adminUserDetailsService; // DB에서 사용자 정보를 가져오는 서비스

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // 1. Authorization 헤더가 없거나 "Bearer "로 시작하지 않으면 통과
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 2. "Bearer " 부분을 제외한 순수 토큰만 추출
            String token = authHeader.substring(7);
            String username = jwtUtil.getUsernameFromToken(token);

            // 3. 토큰에서 username을 성공적으로 추출했고, 아직 인증되지 않은 상태라면
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.adminUserDetailsService.loadUserByUsername(username);

                // 4. 토큰이 유효한지 최종 검증
                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                    // 5. 인증 성공! Spring Security 컨텍스트에 인증 정보 저장
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (ExpiredJwtException e) {
            // 6. JWT 토큰이 만료되었을 경우 발생하는 예외를 여기서 처리합니다.
            //    - 콘솔에 간단한 로그를 남겨 만료 사실을 인지할 수 있도록 합니다.
            //    - 예외를 던지는 대신 아무것도 하지 않고 넘어가면, 해당 요청은 '인증되지 않은' 사용자의 요청으로 처리됩니다.
            logger.info("Could not set user authentication in security context", e);
        }

        // 7. 다음 필터로 요청을 전달합니다.
        filterChain.doFilter(request, response);
    }
}
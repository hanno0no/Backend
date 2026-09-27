package hanno0no.hnn.config;

import hanno0no.hnn.service.admin.AdminUserDetailsService;
import hanno0no.hnn.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AdminUserDetailsService adminUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            int adminId = jwtUtil.getAdminIdFromToken(token);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.adminUserDetailsService.loadUserByAdminId(adminId);

                if (jwtUtil.validateToken(token, adminId)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    // 활동(=인증된 요청)마다 만료 시간을 연장한 토큰을 재발급.
                    // "마지막 활동으로부터 N시간" 방식의 슬라이딩 만료를 구현하기 위함.
                    response.setHeader("New-Access-Token", jwtUtil.generateToken(adminId));
                }
            }
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
            // 만료/위조 토큰이어도 여기서 막지 않음.
            // 공개 API(/index 등)는 permitAll로 통과시키고, 보호 API는 SecurityEntryPoint가 401 처리.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}

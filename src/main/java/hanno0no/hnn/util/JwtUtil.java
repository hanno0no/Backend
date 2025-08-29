package hanno0no.hnn.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // application.properties에서 설정한 시크릿 키와 만료 시간을 가져옵니다.
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    /**
     * 사용자 이름을 기반으로 JWT를 생성합니다.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 토큰의 주체(사용자 이름)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey) // HS256 알고리즘과 시크릿 키로 서명
                .compact();
    }

    // (참고) 나중에 토큰을 검증하는 메소드도 여기에 추가하게 됩니다.
}
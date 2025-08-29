package hanno0no.hnn.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys; // ✨ 1. Keys 임포트 추가
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey; // ✨ 2. SecretKey 임포트 추가
import java.nio.charset.StandardCharsets; // ✨ 3. StandardCharsets 임포트 추가
import java.util.Date;

@Component
public class JwtUtil {

    // application.properties에서 설정한 시크릿 키와 만료 시간을 가져옵니다.
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    // ✨ 4. secretKey 문자열을 기반으로 암호화에 사용할 SecretKey 객체를 생성하는 private 메소드
    private SecretKey getSigningKey() {
// 시크릿 키를 UTF-8 바이트 배열로 변환합니다.
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
// 바이트 배열을 사용해 HMAC-SHA 키를 생성합니다.
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**

     사용자 이름을 기반으로 JWT를 생성합니다.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 토큰의 주체(사용자 이름)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 토큰 만료 시간
// ✨ 5. 문자열 대신 getSigningKey() 메소드가 반환하는 Key 객체로 서명합니다.
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**

     토큰에서 사용자 이름(subject)을 추출합니다.
     */
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**

     토큰이 만료되었는지 확인합니다.
     */
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    /**

     토큰의 유효성을 검증합니다.
     */
    public boolean validateToken(String token, String username) {
        final String usernameFromToken = getUsernameFromToken(token);
        return (usernameFromToken.equals(username) && !isTokenExpired(token));
    }

    /**

     토큰을 파싱하여 클레임(정보)을 추출합니다.

     이 과정에서 서명 검증이 자동으로 이루어집니다.
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
// ✨ 6. 토큰을 검증(파싱)할 때도 동일한 Key 객체를 사용합니다.
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
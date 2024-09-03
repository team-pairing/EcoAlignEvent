package kr.ac.kopo.ecoalignbackend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET_KEY}") // application.properties 에서 키 값을 주입받음
    private String secretKey;

    private SecretKey SECRET_KEY;

    @PostConstruct
    public void init() {
        // Base64로 인코딩된 키를 디코딩하여 SecretKey 객체로 변환
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.SECRET_KEY = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }

    // 1. JWT 토큰 생성 메서드
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // 토큰의 주제, 일반적으로 사용자명
                .setIssuedAt(new Date())  // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // 만료 시간, 여기서는 1시간(60분) 설정
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)  // HS256 알고리즘과 비밀 키로 서명
                .compact();  // 토큰 생성 후 반환
    }

    // 2. 토큰에서 클레임(Claims) 추출
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)  // 서명에 사용된 비밀 키 설정
                .build()
                .parseClaimsJws(token)  // 토큰을 파싱하여 클레임을 추출
                .getBody();
    }

    // 3. 토큰에서 사용자명 추출
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();  // 추출된 클레임에서 주제(사용자명) 추출
    }

    // 4. 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());  // 만료 시간과 현재 시간을 비교하여 만료 여부 반환
    }

    // 5. 토큰 유효성 검사
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);  // 토큰에서 사용자명 추출
        return (extractedUsername.equals(username) && !isTokenExpired(token));  // 토큰의 사용자명과 전달된 사용자명을 비교, 만료되지 않았는지도 확인
    }
}

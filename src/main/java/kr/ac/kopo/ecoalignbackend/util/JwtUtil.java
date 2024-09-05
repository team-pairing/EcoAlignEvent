package kr.ac.kopo.ecoalignbackend.util;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

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
    // Token - subject로 생성, 이메일 인증에 사용
    public String generateToken(String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))  // 3분 유효
                .signWith(SECRET_KEY, Jwts.SIG.HS256)
                .compact();
    }

    // Authentication 정보를 가지고 AccessToken, RefreshToken 생성
    // AccessToken - 자격으로 생성
    public String generateAccessToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(authentication.getName()) // 토큰의 주제, 사용자명
                .claim("auth", authorities) // "auth"라는 클레임, authorities 설정
                .issuedAt(new Date()) // 토큰 발급 시간
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 만료 시간, 여기서는 1시간(60분) 설정
                .signWith(SECRET_KEY, Jwts.SIG.HS256) // HS256 알고리즘과 비밀 키로 서명
                .compact();
    }

    // RefreshToken - 자격으로 생성
    public String generateRefreshToken(Authentication authentication) {
        return Jwts.builder()
                .subject(authentication.getName())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY, Jwts.SIG.HS256)
                .compact();
    }


    // Mapped Token
    public Map<String, Object> generateTokens(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }


    // 2. 토큰에서 클레임(Claims -  토큰 내에 저장된 정보, 페이로드 부분) 추출; 복호화
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY) // 서명에 사용된 비밀 키 설정
                .build()
                .parseSignedClaims(token) // 토큰을 파싱하여 클레임을 추출
                .getPayload();
    }

    // 3. 토큰에서 subject 추출
    public String extractSubject(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getBody()
                .getSubject();
    }

    // 3.  토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());  // 만료 시간과 현재 시간을 비교하여 만료 여부 반환
    }

    // 4. 토큰 유효성 검사
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractClaims(token).getSubject();  // 토큰에서 사용자명 추출
        return (extractedUsername.equals(username) && !isTokenExpired(token));  // 토큰의 사용자명과 전달된 사용자명을 비교, 만료되지 않았는지도 확인
    }
}

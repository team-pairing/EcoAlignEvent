package kr.ac.kopo.ecoalignbackend.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import kr.ac.kopo.ecoalignbackend.dto.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PublicKey;
import java.util.Date;

@Log4j2
@Component
public class JwtUtil {
    @Value("${JWT_SECRET_KEY}") // application.properties 에서 키 값을 주입받음
    private String SECRET_KEY;
    private Key secretKey;
    private long accessTokenExpTime;

    @PostConstruct
    public void init() {
        // Base64로 인코딩된 키를 디코딩하여 SecretKey 객체로 변환
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = 1000 * 60 * 3; // 3분간 유효
    }

    // 1. JWT 생성 : subject로 생성
    // 이메일 인증에 사용
    public Token generateToken(String authCode) {
        String token = Jwts.builder()
                .subject(authCode)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpTime))
                .signWith(secretKey)
                .compact();

        return Token.builder()
                .grantType("Bearer")
                .accessToken(token)
                .build();
    }

    // 2. JWT 생성 : DTO로 생성
    public Token createToken(UserDTO userDTO) {
        Claims claims = (Claims) Jwts.claims();
        claims.put("memberId", userDTO.getMemberId());
        claims.put("name", userDTO.getName());
        claims.put("role", userDTO.getRole());

        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpTime))
                .signWith(secretKey)
                .compact();

        return Token.builder()
                .grantType("Bearer")
                .accessToken(token)
                .build();
    }

    // 3. JWT Claims 추출
    public Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith((SecretKey) secretKey).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    // 4. Claims에서 User Id(memberId) 추출
    public String getUserId(String token){
        return parseClaims(token).get("memberId", String.class);
    }

    // 5. Claims에서 Subject 추출
    public String extractSubject(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // 5. JWT 검증
    public boolean validateToken(String token){
        try {
            Jwts.parser().verifyWith((SecretKey) secretKey).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    // 6. 요청에서 token 추출
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }
}

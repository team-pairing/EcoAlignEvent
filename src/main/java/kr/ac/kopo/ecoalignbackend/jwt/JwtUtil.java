package kr.ac.kopo.ecoalignbackend.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import kr.ac.kopo.ecoalignbackend.dto.UserDTO;
import kr.ac.kopo.ecoalignbackend.entity.UserEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Component
public class JwtUtil {
    @Value("${JWT_SECRET_KEY}") // application.properties 에서 키 값을 주입받음
    private String SECRET_KEY;
    private Key secretKey;
    private long accessTokenExpTime;
    private long authTokenExpTime;

    @PostConstruct
    public void init() {
        // Base64로 인코딩된 키를 디코딩하여 SecretKey 객체로 변환
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = 1000 * 60 * 60; // 60분간 유효
        this.authTokenExpTime = 1000 * 60 * 3; // 3분간 유효
    }

    // 1. JWT 생성 : subject로 생성
    // 이메일 인증에 사용
    public Token generateToken(String authCode) {
        String token = Jwts.builder()
                .subject(authCode)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + authTokenExpTime))
                .signWith(secretKey)
                .compact();

        return Token.builder()
                .grantType("Bearer")
                .accessToken(token)
                .build();
    }

    // 2. JWT 생성 : DTO로 생성
    public Token createToken(UserDTO userDTO) {
        // JWT 토큰 생성
        String token = Jwts.builder()
                .subject(userDTO.getMemberId())
                .claim("name", userDTO.getName())
                .claim("auth", userDTO.getAuthority())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpTime)) // 1시간 유효
                .signWith(secretKey) // 서명 알고리즘 명시
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
        } catch (SecurityException | MalformedJwtException e) {
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

    // 6-1. token 처리
    public String tokenSorting(String bearerToken){
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }

    // 7. JWT 복호화 : token의 정보 반환
    public Authentication getAuthentication(String accessToken) {
        // claim 추출 - 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("No authentication.");
        }

        // claim에서 권한 정보 추출
        Collections authorities =
                (Collections) Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserEntity user = new UserEntity();
        user.setMemberId(claims.getSubject());
        user.setName((String) claims.get("name"));
        user.setAuthority((List<String>) authorities);

        return new UsernamePasswordAuthenticationToken(user,"", (Collection<? extends GrantedAuthority>) authorities);
    }
}

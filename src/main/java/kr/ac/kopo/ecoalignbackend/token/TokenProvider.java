package kr.ac.kopo.ecoalignbackend.token;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import kr.ac.kopo.ecoalignbackend.entity.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Log4j2
public class TokenProvider implements InitializingBean {

    @Value("${JWT_SECRET_KEY}") // application.properties 에서 키 값을 주입받음
    private String secretKey;
    private static final String AUTHORITIES_KEY = "auth"; //JWT 클레임 정보 저장
    private Key key; // JWT 서명 및 검증에 사용

    // 초기화
    @Override
    public void afterPropertiesSet()  {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication){
        // 사용자의 권한 목록을 반환해 쉼표로 구분된 단일 문자열로 결합
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + 1000 * 60 * 3); // 3분 유효

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key)
                .expiration(validity)
                .compact(); // build
    }

    // jwt payload data : sub, auth, exp
    public Authentication getAuthentication(String token){
        // jwt parsing, claim extract - 사용자 정보, 권한 정보 포함
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // claim에서 권한 정보 추출
        // jwt에서 authority 부분을 포함해 spring security User 생성 후 security context에 저장
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // token 유효성 검사
    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("Wrong JWT Verified");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong JWT verified");
        } catch (ExpiredJwtException e){
            log.info("Expired JWT");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired JWT");
        } catch (UnsupportedJwtException e){
            log.info("Unsupported JWT");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unsupported JWT");
        } catch (IllegalArgumentException e){
            log.info("Wrong JWT");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong JWT");
        }
    }

    // Header에서 token extract
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if
    }
}

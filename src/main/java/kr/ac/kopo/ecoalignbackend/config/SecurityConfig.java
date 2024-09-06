package kr.ac.kopo.ecoalignbackend.config;

import kr.ac.kopo.ecoalignbackend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService; // CustomUserDetailsService 빈 주입

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보안 비활성화
                .csrf().disable()
                // JWT 사용하기 때문에 세션 사용 안함
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 권한 없이도 어떤 요청이던 수행
                );
//                .requestMatchers("").permitAll() // 해당 API는 모든 요청을 허가
//                .requestMatchers("").hasRole("") //
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService; // UserDetailsService 빈으로 등록
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        // BCrypt Encoder 사용
        return new BCryptPasswordEncoder();
    }
}


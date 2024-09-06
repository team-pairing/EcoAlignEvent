package kr.ac.kopo.ecoalignbackend.controller;

import jakarta.mail.MessagingException;
import kr.ac.kopo.ecoalignbackend.dto.CodeDTO;
import kr.ac.kopo.ecoalignbackend.dto.MailDTO;
import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
import kr.ac.kopo.ecoalignbackend.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class MailController {

    private final MailService mailService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MailController(MailService mailService, JwtUtil jwtUtil) {
        this.mailService = mailService;
        this.jwtUtil = jwtUtil;
    }

        @ResponseBody
    @PostMapping("/send")
    public String emailSend(@RequestBody MailDTO mailDTO) throws MessagingException {
        String authCode = mailService.sendSimpleMessage(mailDTO.getEmail());
        String token = jwtUtil.generateToken(authCode); // 인증 코드로 JWT 토큰 생성
        System.out.println("Generated JWT Token: " + token); // JWT 토큰을 출력
        return authCode; // Response body에 값을 반환
    }

    @ResponseBody
    @PostMapping("/check")
    public boolean emailCheck(@RequestBody CodeDTO dto, @RequestHeader("AuthToken") String token) {
        String authCode = jwtUtil.extractSubject(token); // 헤더에 포함되어있는 토큰에서 인증 코드를 추출
        return authCode != null && authCode.equals(dto.getCode());
        // authCode와 사용자가 입력한 code 비교 - 일치하면 true, 불일치하면 false
    }
}


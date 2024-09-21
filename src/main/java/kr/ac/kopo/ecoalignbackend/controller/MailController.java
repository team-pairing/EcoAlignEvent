package kr.ac.kopo.ecoalignbackend.controller;

import jakarta.mail.MessagingException;
import kr.ac.kopo.ecoalignbackend.dto.CodeDTO;
import kr.ac.kopo.ecoalignbackend.dto.MailDTO;
import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
import kr.ac.kopo.ecoalignbackend.jwt.Token;
import kr.ac.kopo.ecoalignbackend.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    // 이메일 인증번호 전송
    @ResponseBody
    @PostMapping("/send")
    public ResponseEntity<?> emailSend(@RequestBody MailDTO mailDTO) throws MessagingException {
        String authCode = mailService.sendSimpleMessage(mailDTO.getEmail());
        if (authCode == null) {
            return ResponseEntity.badRequest().build(); // 실패 시 bad request
        }
        else {
            Token token = jwtUtil.generateToken(authCode); // 인증 코드로 JWT 토큰 생성
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token.getGrantType() + " " + token.getAccessToken());
            Map<String, String> resultBody = new HashMap<>();
            resultBody.put("check_number", authCode);
            return ResponseEntity.status(200).headers(headers).body(resultBody); // Response body에 값을 반환
        }
    }

    // 이메일 인증
    @ResponseBody
    @PostMapping("/check")
    public ResponseEntity<?> emailCheck(@RequestBody CodeDTO dto, @RequestHeader("Authorization") String token) {
        String authCode = jwtUtil.extractSubject(token); // 헤더에 포함되어있는 토큰에서 인증 코드를 추출
        if (authCode != null && dto.getCheckNumber() != null){
            if (authCode.equals(dto.getCheckNumber())) {
                return ResponseEntity.status(200).build(); // 인증에 성공했을 때
            } else {
                return ResponseEntity.badRequest().build(); // 인증에 실패한 경우
            }
        } else {
            return ResponseEntity.noContent().build(); // 입력이 빈 경우
        }
    }
}


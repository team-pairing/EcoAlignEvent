package kr.ac.kopo.ecoalignbackend.controller;

import jakarta.mail.MessagingException;
import kr.ac.kopo.ecoalignbackend.dto.MailDTO;
import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
import kr.ac.kopo.ecoalignbackend.jwt.Token;
import kr.ac.kopo.ecoalignbackend.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "http://192.168.24.189:8081", exposedHeaders = "Authorization")
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

        if (mailDTO.getEmail() == null || mailDTO.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 사용자가 입력 없이 요청한 경우

        } else {
            String authCode = mailService.sendSimpleMessage(mailDTO.getEmail());

            if (authCode == null || authCode.isEmpty()) {
                return ResponseEntity.internalServerError().build(); // 이메일 전송 실패 시

            } else {
                Token token = jwtUtil.generateToken(authCode); // 인증 코드로 JWT 토큰 생성
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", token.getGrantType() + " " + token.getAccessToken());
                System.out.println("Authorization 정보" + token.getAccessToken());
                return ResponseEntity.ok().headers(headers).build(); // 이메일 전송 성공 시
            }
        }
    }

    // 이메일 인증
    @ResponseBody
    @PostMapping("/check")
    public ResponseEntity<?> emailCheck(@RequestBody Map<String, Object> code, @RequestHeader("Authorization") String token) {
        token = jwtUtil.tokenSorting(token);
        if (jwtUtil.validateToken(token)) {
            String authCode = jwtUtil.extractSubject(token); // 헤더에 포함되어있는 토큰에서 인증 코드를 추출
            String checkNumber = (String) code.get("checkNumber");

            if (authCode != null && !authCode.isEmpty() &&
                    checkNumber != null && !checkNumber.isEmpty()) {

                if (authCode.equals(checkNumber)) {
                    return ResponseEntity.ok().build(); // 인증에 성공했을 때

                } else {
                    return ResponseEntity.notFound().build(); // 인증에 실패한 경우
                }

            } else {
                return ResponseEntity.badRequest().build(); // 입력이 빈 경우
            }
        }
        return ResponseEntity.internalServerError().build();
    }
}


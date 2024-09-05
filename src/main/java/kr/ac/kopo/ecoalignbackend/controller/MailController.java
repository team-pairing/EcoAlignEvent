package kr.ac.kopo.ecoalignbackend.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import kr.ac.kopo.ecoalignbackend.dto.CodeDTO;
import kr.ac.kopo.ecoalignbackend.dto.MailDTO;
import kr.ac.kopo.ecoalignbackend.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class MailController {

    private final MailService mailService;
    private final HttpSession session;  // 세션을 이용해 authCode 저장

    @ResponseBody
    @PostMapping("/send")
    public String emailSend(@RequestBody MailDTO mailDTO) throws MessagingException, UnsupportedEncodingException {
        String authCode = mailService.sendSimpleMessage(mailDTO.getEmail());
        session.setAttribute("authCode", authCode);  // 세션에 authCode 저장
        return authCode; // Response body에 값을 반환
    }

    @ResponseBody
    @PostMapping("/check")
    public boolean emailCheck(@RequestBody CodeDTO dto) {
        String authCode = (String) session.getAttribute("authCode");  // 세션에서 authCode 가져오기

        // authCode와 사용자가 입력한 code 비교
        if (authCode != null && authCode.equals(dto.getCode())) {
            return true;  // 일치하면 true 반환
        } else {
            return false;  // 일치하지 않으면 false 반환
        }
    }
}


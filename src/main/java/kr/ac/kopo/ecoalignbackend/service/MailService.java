package kr.ac.kopo.ecoalignbackend.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "kath92786@gmail.com";

    // 랜덤으로 숫자 생성
    public String createNumber() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) { // 인증 코드 8자리
            int index = random.nextInt(2); // 0~2까지 랜덤, 랜덤값으로 switch문 실행

            switch (index) {
//                case 0 -> key.append((char) (random.nextInt(26) + 97)); // 소문자
                case 0 -> key.append((char) (random.nextInt(26) + 65)); // 대문자
                case 1 -> key.append(random.nextInt(10)); // 숫자
            }
        }
        return key.toString();
    }

    public MimeMessage createMail(String mail, String number) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom("EcoAlign < " + senderEmail + " >");
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("[EcoAlign] 인증번호 ");
        String body = "";
        body += "<h3>요청하신 인증 번호입니다.</h3>";
        body += "";
        body += "<h1>" + number + "</h1>";
        message.setText(body, "UTF-8", "html");

        return message;
    }

    // 메일 발송
    public String sendSimpleMessage(String sendEmail) throws MessagingException {
        String number = createNumber(); // 랜덤 인증번호 생성

        MimeMessage message = createMail(sendEmail, number); // 메일 생성
        try {
            javaMailSender.send(message); // 메일 발송
        } catch (MailException e) {
            e.printStackTrace();
            return null;
        }

        return number; // 생성된 인증번호 반환
    }
}


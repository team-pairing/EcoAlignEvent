package kr.ac.kopo.ecoalignbackend.controller;

import jakarta.mail.MessagingException;
import kr.ac.kopo.ecoalignbackend.dto.UserDTO;
import kr.ac.kopo.ecoalignbackend.entity.UserEntity;
import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
import kr.ac.kopo.ecoalignbackend.jwt.Token;
import kr.ac.kopo.ecoalignbackend.service.MailService;
import kr.ac.kopo.ecoalignbackend.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    private final MailService mailService;
    public UserController(UserService userService, JwtUtil jwtUtil, MailService mailService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.mailService = mailService;
    }


    // 아이디 중복 확인
    @PostMapping("/checkId")
    public ResponseEntity<?> checkId(@RequestBody Map<String, Object> requestId) {
        String memberId = (String) requestId.get("memberId");
        if (memberId == null || memberId.isEmpty()) { // 사용자가 입력 없이 요청한 경우
            return ResponseEntity.badRequest().build();
        } else {
            Optional<UserEntity> result = userService.findByMemberId(memberId);
            if (result.isEmpty()) { // 아이디가 데이터베이스에 없는 경우
                return ResponseEntity.accepted().build();
            } else { // 아이디가 데이터베이스에 존재하는 경우
                return ResponseEntity.badRequest().build();
            }
        }
    }

    // 회원가입 - 회원정보 저장
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody Map<String, Object> requestUser) {
        String memberId = (String) requestUser.get("memberId");
        String password = (String) requestUser.get("password");
        String email = (String) requestUser.get("email");
        String name = (String) requestUser.get("name");

        if (memberId != null && !memberId.isEmpty() &&
                password != null && !password.isEmpty() &&
                email != null && !email.isEmpty() &&
                name != null && !name.isEmpty()) {

            UserDTO registeredUser = userService.registerUser(requestUser);
            Token token = jwtUtil.createToken(registeredUser); // 인증 코드로 JWT 토큰 생성
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token.getGrantType() + " " + token.getAccessToken());

            return ResponseEntity.ok().headers(headers).build(); // 회원가입에 성공한 경우

        } else {
            return ResponseEntity.badRequest().build(); // 사용자의 요청 값이 비어있는 경우
        }
    }

    // 회원탈퇴
    @PostMapping("/signOut")
    public ResponseEntity<?> signOut(@RequestBody Map<String, Object> requestUser){
        String memberId = (String) requestUser.get("memberId");
        String password = (String) requestUser.get("password");

        if (memberId == null || memberId.isEmpty() ||
                password == null || password.isEmpty()){ // 사용자 입력이 올바르지 않은 경우
            return ResponseEntity.badRequest().build();

        } else {
            boolean result = userService.deleteUserEntity(memberId, password);
            if (result) { // 정상적으로 회원 탈퇴 된 경우
                return ResponseEntity.ok().build();
            } else { // 정상적으로 회원 탈퇴되지 않은 경우
                return ResponseEntity.notFound().build();
            }
        }
    }

    // 로그인
    @PostMapping("/logIn")
    public ResponseEntity<?> logIn(@RequestBody Map<String, Object> requestUser) {
        String memberId = (String) requestUser.get("memberId");
        String password = (String) requestUser.get("password");

        if (memberId == null || memberId.isEmpty() ||
                password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 사용자의 입력 값이 비었을 때

        } else {
            // 아이디를 가진 사용자가 있는지 확인
            if (userService.findByMemberId(memberId).isPresent()) {
                // 로그인 진행
                Token token = userService.logIn(memberId, password);
                if (token == null) {
                    return ResponseEntity.notFound().build(); // 비밀번호가 일치하지 않을 때
                } else {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", token.getGrantType() + " " + token.getAccessToken());
                    UserEntity userEntity = userService.findUserByMemberId(memberId);
                    Map<String, String> resultBody = new HashMap<>();
                    resultBody.put("name", userEntity.getName());
                    return ResponseEntity.ok().headers(headers).body(resultBody);
                }
            } else {
                return ResponseEntity.unprocessableEntity().build(); // 아이디가 존재하지 않을 때
            }
        }
    }

    // 아이디 찾기
    @GetMapping("/findId")
    public ResponseEntity<?> findId(@RequestBody Map<String, Object> requestUser){
        String name = (String) requestUser.get("name");
        String email = (String) requestUser.get("email");
        String birth = (String) requestUser.get("birth");

        if (name == null || name.isEmpty() ||
                email == null || email.isEmpty() ||
                birth == null || birth.isEmpty()) {

            return ResponseEntity.badRequest().build(); // 사용자 입력이 비어있는 경우

        } else {

            String memberId = userService.findMemberIdByNameAndEmailAndBirth(name, email, birth);
            if (memberId == null) {
                return ResponseEntity.notFound().build(); // 회원이 아닌 경우

            } else {
                requestUser.put("memberId", memberId);
                return ResponseEntity.status(200).body(requestUser); // 아이디를 성공적으로 찾은 경우
            }
        }
    }

    // 비밀번호 찾기

    // 사용자 이메일 인증코드 전송
    public ResponseEntity<?> sendCode(@RequestBody Map<String, Object> requestUser) throws MessagingException {
        String memberId = (String) requestUser.get("memberId");
        String email = (String) requestUser.get("email");

        if (memberId == null || memberId.isEmpty() ||
                email == null || email.isEmpty()) {

            return ResponseEntity.badRequest().build(); // 사용자 입력이 비어있는 경우

        } else {
            if (userService.findPasswordUser(memberId, email).isEmpty()) {
                return ResponseEntity.notFound().build(); // 사용자가 존재하지 않을 경우

            } else {
                String authCode = mailService.sendSimpleMessage(email);

                if (authCode == null || authCode.isEmpty()) {
                    return ResponseEntity.internalServerError().build(); // 이메일 전송 실패 시

                } else {
                    Token token = jwtUtil.generateToken(authCode); // 인증 코드로 JWT 토큰 생성
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", token.getGrantType() + " " + token.getAccessToken());

                    return ResponseEntity.ok().headers(headers).build(); // 이메일 전송 성공 시
                }
            }
        }
    }

    // 사용자 이메일 인증

    // 비밀번호 재설정
    public void updatePw(){}

    // 회원 정보 수정
    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, Object> updates) {
        String memberId = (String) updates.get("memberId");
        UserEntity user = userService.findUserByMemberId(memberId);

        if (user != null) {
            // 각 필드에 대해 업데이트가 필요한지 확인하고 변경
            if (updates.containsKey("email")) {
                user.setEmail((String) updates.get("email"));
            }
            if (updates.containsKey("password")) {
                user.setPassword((String) updates.get("password"));
            }
            if (updates.containsKey("birth")) {
                user.setBirth((String) updates.get("birth"));
            }
            if (updates.containsKey("gender")) {
                user.setGender((String) updates.get("gender"));
            }

            // 변경사항 저장 후 DTO 반환
            UserEntity updatedUser = userService.saveUser(user);
            return ResponseEntity.ok().body(updatedUser); // Response body에 값을 반환
        } else {
            return ResponseEntity.notFound().build(); // notFound status 반환
        }
    }
}


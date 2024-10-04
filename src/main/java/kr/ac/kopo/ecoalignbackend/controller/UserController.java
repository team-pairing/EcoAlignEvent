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

@CrossOrigin(origins = "http://192.168.24.189:8081", exposedHeaders = "Authorization")
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
                return ResponseEntity.status(409).build();
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
            int result = userService.deleteUserEntity(memberId, password);
            return switch (result) {
                case -1 -> ResponseEntity.notFound().build(); // 아이디가 없는 경우

                case 0 -> ResponseEntity.status(403).build(); // 비밀번호가 틀린 경우

                case 1 -> ResponseEntity.ok().build(); // 성공!

                default -> ResponseEntity.internalServerError().build();  // 그 외 오류
            };
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
    @PostMapping("/findId")
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
    @PostMapping("/findPw/sendCode")
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
    @PostMapping("/findPw/checkCode")
    public ResponseEntity<?> checkCode(@RequestBody Map<String, Object> code, @RequestHeader("Authorization") String token) {
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
        } else return ResponseEntity.internalServerError().build();
    }

    // 비밀번호 재설정
    @PostMapping("/findPw/updatePw")
    public ResponseEntity<?> updatePw(@RequestBody Map<String, Object> request){
        String memberId = (String) request.get("memberId");
        String password = (String) request.get("password");

        if (memberId == null || memberId.isEmpty() ||
                password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 입력이 빈 경우
        } else {
            int result = userService.updatePassword(memberId, password);
            if (result == 0) {
                return ResponseEntity.internalServerError().build(); // 수정에 실패한 경우
            } else {
                return ResponseEntity.ok().build(); // 수정에 성공한 경우
            }
        }
    }

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


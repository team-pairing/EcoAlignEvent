package kr.ac.kopo.ecoalignbackend.controller;

import kr.ac.kopo.ecoalignbackend.dto.UserDTO;
import kr.ac.kopo.ecoalignbackend.entity.UserEntity;
import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
import kr.ac.kopo.ecoalignbackend.jwt.Token;
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
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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

            return ResponseEntity.ok().headers(headers).build(); // Response body에 값을 반환

        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 회원탈퇴
    @PostMapping("/signOut")
    public ResponseEntity<?> signOut(@RequestBody Map<String, Object> requestUser){
        String memberId = (String) requestUser.get("memberId");
        String password = (String) requestUser.get("password");
        boolean result = userService.deleteUserEntity(memberId, password);
        if (result) {
            return ResponseEntity.status(200).body(requestUser);
        } else {
            return ResponseEntity.badRequest().body(requestUser);
        }
    }

    // 로그인
    @PostMapping("/logIn")
    public ResponseEntity<?> logIn(@RequestBody Map<String, Object> requestUser) {
        String memberId = (String) requestUser.get("memberId");
        String password = (String) requestUser.get("password");

        // 아이디를 가진 사용자가 있는지 확인
        if (userService.findByMemberId(memberId).isPresent()) {
            // 로그인 진행
            Token token = userService.logIn(memberId, password);
            if (token == null){
                return ResponseEntity.badRequest().build(); // 비밀번호가 일치하지 않을 때
            } else {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", token.getGrantType() + " " + token.getAccessToken());
                UserEntity userEntity= userService.findUserByMemberId(memberId);
                Map<String, String> resultBody = new HashMap<>();
                resultBody.put("name", userEntity.getName());
                resultBody.put("memberId", memberId);
                resultBody.put("password", password);
                return ResponseEntity.status(200).headers(headers).body(resultBody);
            }
        } else {
            return ResponseEntity.badRequest().build(); // 아이디가 존재하지 않을 때
        }
    }

    // 아이디 찾기
    @GetMapping("/findId")
    public ResponseEntity<?> findId(@RequestBody Map<String, Object> requestUser){
        String name = (String) requestUser.get("name");
        String email = (String) requestUser.get("email");
        String birth = (String) requestUser.get("birth");
        String memberId = userService.findMemberIdByNameAndEmailAndBirth(name, email, birth);
        if (memberId == null) {
            return ResponseEntity.badRequest().build();
        } else {
            requestUser.put("memberId", memberId);
            return ResponseEntity.status(200).body(requestUser);
        }
    }
    // 비밀번호 수정
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


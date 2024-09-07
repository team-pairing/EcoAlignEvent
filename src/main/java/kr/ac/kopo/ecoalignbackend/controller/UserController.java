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


    // 회원가입
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody Map<String, Object> requestUser) {
        UserDTO registeredUser = userService.registerUser(requestUser);
        Token token = jwtUtil.createToken(registeredUser); // 인증 코드로 JWT 토큰 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token.getGrantType() + " " + token.getAccessToken());
        return ResponseEntity.ok().headers(headers).body(registeredUser); // Response body에 값을 반환
    }

    // 회원탈퇴
    public void signOut(){}

    // 로그인
    @PostMapping("/logIn")
    public ResponseEntity<?> logIn() {
        return null;
    }

    // 아이디 찾기
    public void findId(){}

    // 비밀번호 수정
    public void updatePw(){}

    // 회원 정보 수정
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        UserEntity user = userService.findById(id);

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


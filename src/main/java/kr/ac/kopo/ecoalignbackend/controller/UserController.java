package kr.ac.kopo.ecoalignbackend.controller;

import kr.ac.kopo.ecoalignbackend.dto.LoginUserDTO;
import kr.ac.kopo.ecoalignbackend.dto.UserDTO;
import kr.ac.kopo.ecoalignbackend.entity.User;
import kr.ac.kopo.ecoalignbackend.service.UserService;
import kr.ac.kopo.ecoalignbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 회원가입
    @PostMapping("/signUp")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody UserDTO userDTO){
        // UserDTO에서 User 엔티티로 변환
        User user = User.builder()
                .memberId(userDTO.getMemberId())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .birth(userDTO.getBirth())
                .gender(userDTO.getGender())
                .build();

        // 비밀번호 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);

        // 사용자 생성 및 저장
        User createdUser = userService.registerUser(user); // 서비스 레이어에서 저장
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("id", createdUser.getId());
        responseMap.put("memberId", createdUser.getMemberId());
        responseMap.put("password", createdUser.getPassword());
        responseMap.put("email", createdUser.getEmail());
        responseMap.put("name", createdUser.getName());
        responseMap.put("birth", createdUser.getBirth());
        responseMap.put("gender", createdUser.getGender());

        System.out.println("Created User: " + responseMap);

        return ResponseEntity.ok(responseMap);
    }

    // 회원탈퇴
    public void signOut(){}

    // 로그인
    @PostMapping("/logIn")
    public ResponseEntity<Map<String, String>> logIn(@RequestBody LoginUserDTO loginUserDTO) {
        System.out.println("Received LoginUserDTO: " + loginUserDTO);

        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDTO.getMemberId(),
                        loginUserDTO.getPassword()
                )
        );


        // 인증된 사용자의 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT 토큰 생성
        String token = jwtUtil.generateAccessToken(authentication);

        // 응답 준비
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("token", token);

        return ResponseEntity.ok(responseMap);
    }

    // 아이디 찾기
    public void findId(){}

    // 비밀번호 수정
    public void updatePw(){}

    // 회원 정보 수정
    @PatchMapping("/update/{id}")
    public UserDTO updateUser(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        Optional<User> user = userService.findById(id);

        if (user.isPresent()) {
            User existingUser = user.get();

            // 각 필드에 대해 업데이트가 필요한지 확인하고 변경
            if (updates.containsKey("email")) {
                existingUser.setEmail((String) updates.get("email"));
            }
            if (updates.containsKey("password")) {
                existingUser.setPassword((String) updates.get("password"));
            }
            if (updates.containsKey("birth")) {
                existingUser.setBirth((String) updates.get("birth"));
            }
            if (updates.containsKey("gender")) {
                existingUser.setGender((String) updates.get("gender"));
            }

            // 변경사항 저장 후 DTO 반환
            User updatedUser = userService.saveUser(existingUser);
            return userService.entityToDto(updatedUser);
        } else {
            return null;
        }
    }
}


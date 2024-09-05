package kr.ac.kopo.ecoalignbackend.controller;

import kr.ac.kopo.ecoalignbackend.dto.UserDTO;
import kr.ac.kopo.ecoalignbackend.entity.User;
import kr.ac.kopo.ecoalignbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/signUp")
    public void signUp(@RequestBody UserDTO userDTO){
        User user = userService.registerUser(userDTO); // 사용자 생성 및 저장
    }

    // 회원탈퇴
    public void signOut(){}

    // 로그인
    public void logIn(){}

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


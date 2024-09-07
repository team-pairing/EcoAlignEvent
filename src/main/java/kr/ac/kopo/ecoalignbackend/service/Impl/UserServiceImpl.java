package kr.ac.kopo.ecoalignbackend.service.Impl;

import kr.ac.kopo.ecoalignbackend.dto.UserDTO;
import kr.ac.kopo.ecoalignbackend.entity.UserEntity;
import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
import kr.ac.kopo.ecoalignbackend.repository.UserRepository;
import kr.ac.kopo.ecoalignbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    // 회원가입
    public UserDTO registerUser(Map<String, Object> requestUser){

        // 입력받은 json 객체로 사용자 생성
        UserDTO userDTO = new UserDTO();
        String id = userDTO.idSetting();
        userDTO.setId(id);
        userDTO.setMemberId((String)requestUser.get("memberId"));
        userDTO.setPassword((String)requestUser.get("password"));
        userDTO.setEmail((String)requestUser.get("email"));
        userDTO.setName((String)requestUser.get("name"));

        if (requestUser.get("birth") != null) {
            userDTO.setBirth((String)requestUser.get("birth"));
        } else {
            userDTO.setBirth("19000101"); // default
        }

        if (requestUser.get("gender") != null) {
            userDTO.setGender((String)requestUser.get("gender"));
        } else {
            userDTO.setGender("male"); //default
        }

        // 비밀번호 암호화
        userDTO.setAuthority(List.of("USER"));
        UserEntity entity = dtoToEntity(userDTO);
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        entity.setPassword(encodedPassword);
        // 사용자 저장
        userRepository.save(entity);

        // 생성된 사용자 객체 반환
        return userDTO;
    }

    // 로그인
    public String logIn(){
        return null;
    }

    // 아이디와 비밀번호로 사용자 찾기
    public Optional<UserEntity> findUserByMemberIdAndPassword(){
        return null;
    }

    // 아이디 찾기
    public Optional<UserEntity> findMemberIdbyNameAndEmail(){
        return null;
    }

    // 비밀번호 수정할 유저 찾기
    public Optional<UserEntity> findPasswordUser(){
        return null;
    }

    // 비밀번호 수정
    public void updatePassword(){
    }

    // 아이디로 사용자 조회 - 회원정보 수정에 필요
    public UserEntity findById(String id){
        Optional<UserEntity> user = userRepository.findByMemberId(id);
        return user.orElse(null);
    }

    // 사용자 변경사항 저장 - 회원정보 수정에 필요
    public UserEntity saveUser(UserEntity user){
        return userRepository.save(user);
    }
}

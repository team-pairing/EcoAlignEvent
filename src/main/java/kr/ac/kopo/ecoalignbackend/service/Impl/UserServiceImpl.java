package kr.ac.kopo.ecoalignbackend.service.Impl;

import kr.ac.kopo.ecoalignbackend.dto.UserDTO;
import kr.ac.kopo.ecoalignbackend.entity.UserEntity;
import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
import kr.ac.kopo.ecoalignbackend.jwt.Token;
import kr.ac.kopo.ecoalignbackend.repository.UserRepository;
import kr.ac.kopo.ecoalignbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
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

    // 아이디를 가진 사용자가 존재하는지 확인
    public Optional<UserEntity> findByMemberId(String memberId){
        return userRepository.findByMemberId(memberId);
    }

    // 로그인
    public Token logIn(String memberId, String password){
        UserEntity requestUser = userRepository.findUserByMemberId(memberId);

        // 암호화된 비밀번호와 일치하는지 확인
        String encodedPassword = requestUser.getPassword();
        // 일치할 경우 권한을 확인하고 없으면 USER 권한 부여
        if (passwordEncoder.matches(password, encodedPassword)){
            if (requestUser.getAuthorities().stream().noneMatch(authority -> authority.getAuthority().equals("USER"))){
                requestUser.setAuthority(List.of("USER"));
            }
            // USER 권한이 부여된 토큰 생성
            return jwtUtil.createToken(entityToDto(requestUser));
        } else {
            // 비밀번호가 일치하지 않을 때
            log.info("Wrong password");
            return null;
        }
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

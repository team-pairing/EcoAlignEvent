package kr.ac.kopo.ecoalignbackend.service.Impl;

import jakarta.transaction.Transactional;
import kr.ac.kopo.ecoalignbackend.dto.*;
import kr.ac.kopo.ecoalignbackend.entity.User;
import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
import kr.ac.kopo.ecoalignbackend.repository.UserRepository;
import kr.ac.kopo.ecoalignbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    // 회원가입
    @Override
    public User registerUser(User entity){
        userRepository.save(entity); // 객체 저장
        return entity;
    };

    // 회원탈퇴
    @Override
    public String logIn(LoginUserDTO loginUserDTO){
        String memberId = loginUserDTO.getMemberId();
        String password = loginUserDTO.getPassword();
        User user = userRepository.findByMemberId(memberId);
        // 아이디(memberId)가 존재하지 않을 떄
        if (user == null){
            throw new UsernameNotFoundException("User doesn't exist.");
        }
        // 비밀번호가 일치하지 않을 때
        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("Wrong password.");
        }

        CustomUserInfoDTO info = modelMapper.map(user, CustomUserInfoDTO.class);

        String accessToken = jwtUtil.createToken(info);
        return accessToken;
    };


    // 아이디와 비밀번호로 사용자 찾기
    @Override
    public Optional<User> findUserByMemberIdAndPassword(LoginUserDTO dto){
        return userRepository.findUserByMemberIdAndPassword(dto.getMemberId(), dto.getPassword());
    };

    // 아이디 찾기
    @Override
    public Optional<User> findMemberIdbyNameAndEmail(FindIdUserDTO dto){
        return userRepository.findUserByNameAndEmail(dto.getName(), dto.getEmail());
    };

    // 비밀번호 수정할 사용자 찾기
    @Override
    public Optional<User> findPasswordUser(FindPwUserDTO dto){
        return null;
    };

    // 비밀번호 수정
//    public

    // 아이디로 사용자 조회 - 회원정보 수정에 필요
    @Override
    public Optional<User> findById(String id){
        return userRepository.findUserById(id);
    }

    // 사용자 변경사항 저장 - 회원정보 수정에 필요
    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

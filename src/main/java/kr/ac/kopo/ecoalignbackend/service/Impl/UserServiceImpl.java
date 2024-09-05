package kr.ac.kopo.ecoalignbackend.service.Impl;

import kr.ac.kopo.ecoalignbackend.dto.FindIdUserDTO;
import kr.ac.kopo.ecoalignbackend.dto.FindPwUserDTO;
import kr.ac.kopo.ecoalignbackend.dto.LoginUserDTO;
import kr.ac.kopo.ecoalignbackend.dto.UserDTO;
import kr.ac.kopo.ecoalignbackend.entity.User;
import kr.ac.kopo.ecoalignbackend.repository.UserRepository;
import kr.ac.kopo.ecoalignbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // 회원가입
    public User registerUser(UserDTO dto){
        User user = dtoToEntity(dto);
        userRepository.save(user); // 객체 저장
        return user;
    };

    // 회원탈퇴
    public void deleteUser(UserDTO dto){
        userRepository.deleteUserById(dto.getId());
    };

    // 로그인
    public Optional<User> loginUser(LoginUserDTO dto){
        return userRepository.findUserByIdAndPassword(dto.getId(), dto.getPassword());
    };

    // 아이디 찾기
    public Optional<User> findIdbyNameAndEmail(FindIdUserDTO dto){
        return userRepository.findUserByNameAndEmail(dto.getName(), dto.getEmail());
    };

    // 비밀번호 수정할 유저 찾기
    public Optional<User> findPasswordUser(FindPwUserDTO dto){
        return null;
    };

    // 비밀번호 수정
//    public
}

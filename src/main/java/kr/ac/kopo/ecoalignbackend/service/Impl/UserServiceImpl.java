package kr.ac.kopo.ecoalignbackend.service.Impl;

import jakarta.transaction.Transactional;
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
    @Override
    public User registerUser(UserDTO dto){
        User user = dtoToEntity(dto);
        userRepository.save(user); // 객체 저장
        return user;
    };

    // 회원탈퇴
    @Override
    public void deleteUser(UserDTO dto){
        userRepository.deleteUserById(dto.getId());
    };

    // 아이디와 비밀번호로 사용자 찾기
    @Override
    public Optional<User> findUserByIdAndPassword(LoginUserDTO dto){
        return userRepository.findUserByIdAndPassword(dto.getMember_id(), dto.getPassword());
    };

    // 아이디 찾기
    @Override
    public Optional<User> findIdbyNameAndEmail(FindIdUserDTO dto){
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

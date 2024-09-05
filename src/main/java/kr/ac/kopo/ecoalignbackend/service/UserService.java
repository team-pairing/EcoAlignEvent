package kr.ac.kopo.ecoalignbackend.service;

import kr.ac.kopo.ecoalignbackend.dto.FindIdUserDTO;
import kr.ac.kopo.ecoalignbackend.dto.FindPwUserDTO;
import kr.ac.kopo.ecoalignbackend.dto.LoginUserDTO;
import kr.ac.kopo.ecoalignbackend.dto.UserDTO;
import kr.ac.kopo.ecoalignbackend.entity.User;

import java.util.Optional;

public interface UserService {

    // 회원가입
    User registerUser(UserDTO dto);

    // 회원 탈퇴
    void deleteUser(UserDTO dto);

    // 아이디와 비밀번호로 사용자 찾기
    Optional<User> findUserByIdAndPassword(LoginUserDTO dto);

    // 아이디 찾기
    Optional<User> findIdbyNameAndEmail(FindIdUserDTO dto);

    // 비밀번호 수정할 유저 찾기
    Optional<User> findPasswordUser(FindPwUserDTO dto);

    // 비밀번호 수정

    // 아이디로 사용자 조회 - 회원정보 수정에 필요
    Optional<User> findById(String id);

    // 사용자 변경사항 저장 - 회원정보 수정에 필요
    User saveUser(User user);

    // DTO를 Entity로 변환
    default User dtoToEntity(UserDTO dto){
        return User.builder()
                .id(dto.getId())
                .member_id(dto.getMember_id())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .name(dto.getName())
                .birth(dto.getBirth())
                .gender(dto.getGender())
                .build();
    }

    // Entity를 DTO로 변환
    default UserDTO entityToDto(User entity){
        return UserDTO.builder()
                .id(entity.getId())
                .member_id(entity.getMember_id())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .name(entity.getName())
                .birth(entity.getBirth())
                .gender(entity.getGender())
                .build();
    }
}
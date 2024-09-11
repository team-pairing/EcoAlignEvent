package kr.ac.kopo.ecoalignbackend.service;

import kr.ac.kopo.ecoalignbackend.dto.*;
import kr.ac.kopo.ecoalignbackend.entity.UserEntity;
import kr.ac.kopo.ecoalignbackend.jwt.Token;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    // 회원가입
    UserDTO registerUser(Map<String, Object> requestUser);

    // 아이디를 가진 사용자가 존재하는지 확인
    Optional<UserEntity> findByMemberId(String memberId);

    // 아이디를 가진 사용자 찾기
    UserEntity findUserByMemberId(String memberId);

    // 로그인
    Token logIn(String memberId, String password);

    // 아이디와 비밀번호로 사용자 찾기
    Optional<UserEntity> findUserByMemberIdAndPassword();

     // 아이디 찾기
    Optional<UserEntity> findMemberIdbyNameAndEmail();

    // 비밀번호 수정할 유저 찾기
    Optional<UserEntity> findPasswordUser();

    // 비밀번호 수정
    void updatePassword();

    // 사용자 변경사항 저장 - 회원정보 수정에 필요
    UserEntity saveUser(UserEntity user);

    // 사용자 생성
    UserEntity createUserEntity(UserEntity userEntity);

    // 사용자 삭제
    boolean deleteUserEntity(String memberId, String password);

    // DTO를 Entity로 변환
    default UserEntity dtoToEntity(UserDTO dto){
        UserEntity entity = new UserEntity();
        entity.setMemberId(dto.getMemberId());
        entity.setPassword(dto.getPassword());
        entity.setEmail(dto.getEmail());
        entity.setName(dto.getName());
        entity.setBirth(dto.getBirth());
        entity.setGender(dto.getGender());
        entity.setAuthority(dto.getAuthority());
        return entity;
    }

    // Entity를 DTO로 변환
    default UserDTO entityToDto(UserEntity entity){
        return UserDTO.builder()
                .memberId(entity.getMemberId())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .name(entity.getName())
                .birth(entity.getBirth())
                .gender(entity.getGender())
                .authority(entity.getAuthority())
                .build();
    }
}
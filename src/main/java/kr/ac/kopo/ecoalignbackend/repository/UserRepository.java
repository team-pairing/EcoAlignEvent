package kr.ac.kopo.ecoalignbackend.repository;

import kr.ac.kopo.ecoalignbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 이름과 이메일, 생년월일로 사용자 검색
    UserEntity findUserByNameAndEmailAndBirth(String name, String email, String birth);

    // 이름과 이메일, 생년월일로 사용자 유무 확인
    Optional<UserEntity> findByNameAndEmailAndBirth(String name, String email, String birth);

    // 아이디로 사용자 검색
    UserEntity findUserByMemberId(String memberId);

    // 아이디로 사용자 유무 확인
    Optional<UserEntity> findByMemberId(String memberId);

    // 아이디와 이메일로 사용자 유무 확인
    Optional<UserEntity> findBymemberIdAndEmail(String memberId,String email);

    // 아이디랑 비밀번호로 사용자 삭제
    boolean deleteUserEntityByMemberIdAndPassword(String memberId, String password);
}
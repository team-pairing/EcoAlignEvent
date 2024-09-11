package kr.ac.kopo.ecoalignbackend.repository;

import kr.ac.kopo.ecoalignbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 이름과 이메일로 사용자 검색
    Optional<UserEntity> findUserByNameAndEmail(String name, String email);

    // 아이디로 사용자 검색
    UserEntity findUserByMemberId(String memberId);

    // 아이디(memberId)로 사용자 검색
    // 로그인에 사용
    Optional<UserEntity> findByMemberId(String memberId);

    // 아이디랑 비밀번호로 사용자 삭제
    boolean deleteUserEntityByMemberIdAndPassword(String memberId, String password);
}
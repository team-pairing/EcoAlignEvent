package kr.ac.kopo.ecoalignbackend.repository;

import kr.ac.kopo.ecoalignbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 이름과 이메일로 사용자 검색
    Optional<User> findUserByNameAndEmail(String name, String email);

    // 아이디로 사용자 검색
    Optional<User> findUserById(String id);

    // 아이디와 이메일로 사용자 검색
    Optional<User> findUserByIdAndEmail(String id, String email);

    // 아이디와 이메일로 사용자 검색
    Optional<User> findUSerByMemberIdAndPassword(String memberId, String password);

    // 아이디로 회원 탈퇴
    Optional<User> deleteUserById (String id);
}
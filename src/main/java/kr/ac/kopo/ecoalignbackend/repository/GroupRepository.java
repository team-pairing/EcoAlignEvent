package kr.ac.kopo.ecoalignbackend.repository;

import kr.ac.kopo.ecoalignbackend.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    Optional<GroupEntity> findById(String id);
    Optional<GroupEntity> findByGroupItem(String GroupItem);
    List<GroupEntity> findAllByMemberId(String memberId);
}

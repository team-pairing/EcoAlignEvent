package kr.ac.kopo.ecoalignbackend.repository;

import kr.ac.kopo.ecoalignbackend.entity.MemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemoRepository extends JpaRepository<MemoEntity, Long> {
    Optional<MemoEntity> findById(String id);
}

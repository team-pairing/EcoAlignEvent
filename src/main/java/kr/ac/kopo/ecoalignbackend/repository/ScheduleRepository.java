package kr.ac.kopo.ecoalignbackend.repository;

import kr.ac.kopo.ecoalignbackend.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    // 아이디로 일정 검색
    Optional<ScheduleEntity> findScheduleEntityById(String id);

    // 일정 종류(group)으로 모든 일정 삭제
    void deleteAllByKind(String kind);
}

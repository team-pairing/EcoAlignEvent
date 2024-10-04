package kr.ac.kopo.ecoalignbackend.service;

import kr.ac.kopo.ecoalignbackend.dto.ScheduleDTO;
import kr.ac.kopo.ecoalignbackend.entity.ScheduleEntity;

import java.util.List;
import java.util.Map;

public interface ScheduleService {

    // 일정 추가
    void addSchedule(Map<String, Object> request);

    // 일정 수정
    void updateSchedule(ScheduleDTO scheduleDTO);

    // 일정 삭제
    void deleteSchedule(String scheduleId);

    // 일정 조회
    List<ScheduleEntity> allSchedule();

    // 토큰 검증
    boolean validateAuth(String token);
    // 헤더에서 아이디 식별
    String getMemberId(String token);
}

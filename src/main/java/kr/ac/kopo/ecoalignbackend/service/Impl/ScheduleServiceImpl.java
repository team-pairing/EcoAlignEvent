package kr.ac.kopo.ecoalignbackend.service.Impl;

import kr.ac.kopo.ecoalignbackend.dto.ScheduleDTO;
import kr.ac.kopo.ecoalignbackend.entity.ScheduleEntity;
import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
import kr.ac.kopo.ecoalignbackend.repository.ScheduleRepository;
import kr.ac.kopo.ecoalignbackend.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final JwtUtil jwtUtil;

    // 일정 추가
    public void addSchedule(Map<String, Object> request){

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId();
        schedule.setColor((String) request.get("color"));
        schedule.setDetails((String) request.get("details"));
        schedule.setEnd((String) request.get("end"));
        schedule.setKind((String) request.get("kind"));
        schedule.setName((String) request.get("name"));
        schedule.setStart((String) request.get("start"));
        schedule.setTimed((boolean) request.get("timed"));

        scheduleRepository.save(schedule);
    };

    // 일정 수정
    public void updateSchedule(ScheduleDTO scheduleDTO) {

        if (scheduleRepository.findScheduleEntityById(scheduleDTO.getId()).isPresent()) {

            ScheduleEntity schedule = scheduleRepository.findScheduleEntityById(scheduleDTO.getId()).get();

            if (!schedule.getColor().equals(scheduleDTO.getColor())) schedule.setColor(scheduleDTO.getColor());
            if (!schedule.getDetails().equals(scheduleDTO.getDetails())) schedule.setDetails(scheduleDTO.getDetails());
            if (!schedule.getEnd().equals(scheduleDTO.getEnd())) schedule.setEnd(scheduleDTO.getEnd());
            if (!schedule.getKind().equals(scheduleDTO.getKind())) schedule.setKind(scheduleDTO.getKind());
            if (!schedule.getName().equals(scheduleDTO.getName())) schedule.setName(scheduleDTO.getName());
            if (!schedule.getStart().equals(scheduleDTO.getStart())) schedule.setStart(scheduleDTO.getStart());
            if (!schedule.isTimed() == scheduleDTO.isTimed()) schedule.setTimed(scheduleDTO.isTimed());
            scheduleRepository.save(schedule);
        }
    }

    // 일정 삭제
    public void deleteSchedule(String scheduleId){
        if (scheduleRepository.findScheduleEntityById(scheduleId).isPresent()) {
            ScheduleEntity schedule = scheduleRepository.findScheduleEntityById(scheduleId).get();
            scheduleRepository.delete(schedule);
        }
    }

    // 일정 조회
    public List<ScheduleEntity> allSchedule(){
        return scheduleRepository.findAll();
    }

    // 토큰 검증
    public boolean validateAuth(String token) {
        token = jwtUtil.tokenSorting(token);
        return jwtUtil.validateToken(token);
    }
}

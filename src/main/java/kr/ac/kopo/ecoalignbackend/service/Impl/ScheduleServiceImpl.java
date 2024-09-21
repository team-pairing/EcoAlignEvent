package kr.ac.kopo.ecoalignbackend.service.Impl;

import kr.ac.kopo.ecoalignbackend.dto.ScheduleDTO;
import kr.ac.kopo.ecoalignbackend.entity.ScheduleEntity;
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

            if (!schedule.getColor().equals(scheduleDTO.getColor())) schedule.setColor(schedule.getColor());
            if (!schedule.getDetails().equals(scheduleDTO.getDetails())) schedule.setDetails(schedule.getDetails());
            if (!schedule.getEnd().equals(scheduleDTO.getEnd())) schedule.setEnd(schedule.getEnd());
            if (!schedule.getKind().equals(scheduleDTO.getKind())) schedule.setKind(schedule.getKind());
            if (!schedule.getName().equals(scheduleDTO.getName())) schedule.setName(schedule.getName());
            if (!schedule.getStart().equals(scheduleDTO.getStart())) schedule.setStart(schedule.getStart());
            if (!schedule.isTimed() == scheduleDTO.isTimed()) schedule.setTimed(schedule.isTimed());
            scheduleRepository.save(schedule);
        }
    }

    // 일정 삭제
    public void deleteSchedule(ScheduleDTO scheduleDTO){
        if (scheduleRepository.findScheduleEntityById(scheduleDTO.getId()).isPresent()) {
            ScheduleEntity schedule = scheduleRepository.findScheduleEntityById(scheduleDTO.getId()).get();
            scheduleRepository.delete(schedule);
        }
    }

    // 일정 조회
    public List<ScheduleEntity> allSchedule(){
        return scheduleRepository.findAll();
    }
}

package kr.ac.kopo.ecoalignbackend.controller;

import kr.ac.kopo.ecoalignbackend.dto.ScheduleDTO;
import kr.ac.kopo.ecoalignbackend.entity.ScheduleEntity;
import kr.ac.kopo.ecoalignbackend.service.ScheduleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 일정 추가
    @PostMapping("/addSchedule")
    public void addSchedule(Map<String, Object> request){
        scheduleService.addSchedule(request);
    }

    // 일정 수정
    @PostMapping("/updateSchedule")
    public void updateSchedule(ScheduleDTO request) {
        scheduleService.updateSchedule(request);
    }

    // 일정 삭제
    @PostMapping("/deleteSchedule")
    public void deleteSchedule(ScheduleDTO request) {
        scheduleService.deleteSchedule(request);
    }

    // 일정 조회
    @GetMapping("/allSchedule")
    public List<ScheduleEntity> allSchedule() {
        return scheduleService.allSchedule();
    }
}

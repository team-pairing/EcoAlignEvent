package kr.ac.kopo.ecoalignbackend.controller;

import kr.ac.kopo.ecoalignbackend.dto.ScheduleDTO;
import kr.ac.kopo.ecoalignbackend.entity.ScheduleEntity;
import kr.ac.kopo.ecoalignbackend.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<?> addSchedule(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token){
        if (scheduleService.validateAuth(token)) {
            scheduleService.addSchedule(request);
            return ResponseEntity.ok().build(); // 일정 추가 성공
        } else {
            return ResponseEntity.internalServerError().build(); // 사용자 토큰 만료 시 오류
        }
    }

    // 일정 수정
    @PostMapping("/updateSchedule")
    public ResponseEntity<?> updateSchedule(@RequestBody ScheduleDTO request, @RequestHeader("Authorization") String token) {
        if (scheduleService.validateAuth(token)) {
            scheduleService.updateSchedule(request);
            return ResponseEntity.ok().build(); // 일정 수정 성공
        } else {
            return ResponseEntity.internalServerError().build(); // 사용자 토큰 만료 시 오류
        }
    }

    // 일정 삭제
    @PostMapping("/deleteSchedule")
    public ResponseEntity<?> deleteSchedule(@RequestBody Map<String, Object> scheduleId, @RequestHeader("Authorization") String token) {
        if (scheduleService.validateAuth(token)) {
            String id = (String) scheduleId.get("id");
            scheduleService.deleteSchedule(id);
            return ResponseEntity.ok().build(); // 일정 삭제 성공
        } else {
            return ResponseEntity.internalServerError().build(); // 사용자 토큰 만료 시 오류
        }
    }

    // 일정 조회
    @GetMapping("/allSchedule")
    public ResponseEntity<?> allSchedule(@RequestHeader("Authorization") String token) {
        if (scheduleService.validateAuth(token)) {
            Map<String, List<ScheduleEntity>> resultBody = new HashMap<>();
            resultBody.put("allSchedule", scheduleService.allSchedule());
            return ResponseEntity.ok().body(resultBody); // 일정 조회 성공
        } else {
            return ResponseEntity.internalServerError().build(); // 사용자 토큰 만료 시 오류
        }
    }
}

package kr.ac.kopo.ecoalignbackend.controller;

import kr.ac.kopo.ecoalignbackend.dto.MemoDTO;
import kr.ac.kopo.ecoalignbackend.entity.MemoEntity;
import kr.ac.kopo.ecoalignbackend.service.MemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/memo")
public class MemoController {
    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    // 메모 추가
    @PostMapping("/addMemo")
    public ResponseEntity<?> addMemo(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token){
        if (memoService.validateAuth(token)) {
            memoService.addMemo(request);
            return ResponseEntity.ok().build(); // 추가 성공
        } else return ResponseEntity.internalServerError().build(); // 토큰 만료 시 에러
    }

    // 메모 수정
    @PostMapping("/updateMemo")
    public ResponseEntity<?> updateMemo(@RequestBody MemoDTO memoDTO, @RequestHeader("Authorization") String token){
        if (memoService.validateAuth(token)) {
            memoService.updateMemo(memoDTO);
            return ResponseEntity.ok().build(); // 수정 성공
        } else return ResponseEntity.internalServerError().build(); // 토큰 만료 시 에러
    }

    // 메모 삭제
    @PostMapping("/deleteMemo")
    public ResponseEntity<?> deleteMemo(@RequestBody Map<String, Object> requestMemo, @RequestHeader("Authorization") String token){
        if (memoService.validateAuth(token)) {
            String memoId = (String) requestMemo.get("id");
            memoService.deleteMemo(memoId);
            return ResponseEntity.ok().build(); // 삭제 성공
        } else return ResponseEntity.internalServerError().build(); // 토큰 만료 시 에러
    }

    // 메모 조회
    @GetMapping("/allMemo")
    public ResponseEntity<?> allMemo(@RequestHeader("Authorization") String token){
        if (memoService.validateAuth(token)) {
            return ResponseEntity.ok().body(memoService.allMemo()); // 조회 성공
        } else return ResponseEntity.internalServerError().build(); // 토큰 만료 시 에러
    }
}

package kr.ac.kopo.ecoalignbackend.controller;

import kr.ac.kopo.ecoalignbackend.entity.GroupEntity;
import kr.ac.kopo.ecoalignbackend.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // 그룹 추가
    @PostMapping("/addGroup")
    public ResponseEntity<?> addGroup (@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token) {
        if (groupService.validateAuth(token)) {
            String groupItem = (String) request.get("groupItem");
            if (groupService.checkGroupItem(groupItem)) {
                return ResponseEntity.status(409).build(); // 중복된 그룹 존재할 경우
            } else {
                groupService.addGroup(groupItem);
                return ResponseEntity.ok().build(); // 성공적으로 그룹을 추가한 경우
            }
        } else return ResponseEntity.internalServerError().build(); // 토큰 만료 시 에러
    }

    // 그룹 삭제
    @PostMapping("/deleteGroup")
    public ResponseEntity<?> deleteGroup(@RequestBody Map<String, Object> requestGroup, @RequestHeader("Authorization") String token){
        if (groupService.validateAuth(token)) {
            String id = (String) requestGroup.get("id");
            String groupItem = (String) requestGroup.get("groupItem");
            groupService.deleteGroup(id, groupItem);
            return ResponseEntity.ok().build(); // 그룹 삭제 성공
        } else return ResponseEntity.internalServerError().build(); // 토큰 만료 시 에러
    }

    // 그룹 조회
    @GetMapping("/allGroup")
    public ResponseEntity<?> allGroup(@RequestHeader("Authorization") String token) {
        if (groupService.validateAuth(token)) {
            return ResponseEntity.ok().body(groupService.getAllGroups()); // 그룹 조회 성공
        } else return ResponseEntity.internalServerError().build(); // 토큰 만료 시 에러
    }
}

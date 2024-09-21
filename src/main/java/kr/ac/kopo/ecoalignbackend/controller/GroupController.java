package kr.ac.kopo.ecoalignbackend.controller;

import kr.ac.kopo.ecoalignbackend.entity.GroupEntity;
import kr.ac.kopo.ecoalignbackend.service.GroupService;
import org.springframework.web.bind.annotation.*;

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
    public void addGroup (@RequestBody Map<String, Object> request) {
        String groupItem = (String) request.get("groupItem");
        groupService.addGroup(groupItem);
    }

    // 그룹 삭제
    @PostMapping("/deleteGroup")
    public void deleteGroup(@RequestBody Map<String, Object> requestGroup){
        String id = (String) requestGroup.get("id");
        String groupItem = (String) requestGroup.get("groupItem");

        groupService.deleteGroup(id, groupItem);
    }

    // 그룹 조회
    @GetMapping("/allGroup")
    public List<GroupEntity> allGroup() {
        return groupService.getAllGroups();
    }
}

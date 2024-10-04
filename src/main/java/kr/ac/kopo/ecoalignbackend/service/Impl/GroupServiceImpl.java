package kr.ac.kopo.ecoalignbackend.service.Impl;

import kr.ac.kopo.ecoalignbackend.entity.GroupEntity;
import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
import kr.ac.kopo.ecoalignbackend.repository.GroupRepository;
import kr.ac.kopo.ecoalignbackend.repository.ScheduleRepository;
import kr.ac.kopo.ecoalignbackend.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final ScheduleRepository scheduleRepository;
    private final JwtUtil jwtUtil;

    // 그룹 추가
    public void addGroup(String groupItem, String memberId) {
        GroupEntity group = new GroupEntity();
        group.setGroupItem(groupItem);
        group.setMemberId(memberId);
        group.setId();
        groupRepository.save(group);
    }

    // 그룹 삭제
    public boolean deleteGroup(String id, String groupItem) {
        Optional<GroupEntity> group = groupRepository.findById(id);

        if (group.isPresent() && group.get().getGroupItem().equals(groupItem)) {
            scheduleRepository.deleteAllByKind(groupItem); // 해당 그룹에 속해있는 일정들 삭제
            groupRepository.delete(group.get());
            return true;
        }
        return false;
    }

    // 모든 그룹 목록 조회
    public List<GroupEntity> getAllGroups(String memberId) {
        return groupRepository.findAllByMemberId(memberId);
    }

    // 토큰 검증
    public boolean validateAuth(String token) {
        token = jwtUtil.tokenSorting(token);
        return jwtUtil.validateToken(token);
    }

    // 그룹 중복 확인
    public boolean checkGroupItem(String groupItem) {
        return groupRepository.findByGroupItem(groupItem).isPresent();
    }

    // 헤더에서 아이디 식별
    public String getMemberId(String token) {
        return jwtUtil.getMemberId(token);
    }
}

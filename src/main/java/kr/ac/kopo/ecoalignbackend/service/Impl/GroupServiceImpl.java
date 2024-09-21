package kr.ac.kopo.ecoalignbackend.service.Impl;

import kr.ac.kopo.ecoalignbackend.entity.GroupEntity;
import kr.ac.kopo.ecoalignbackend.repository.GroupRepository;
import kr.ac.kopo.ecoalignbackend.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    // 그룹 추가
    public void addGroup(String groupItem) {
        GroupEntity group = new GroupEntity();
        group.setGroupItem(groupItem);
        group.setId();
        groupRepository.save(group);
    }

    // 그룹 삭제
    public boolean deleteGroup(String id, String groupItem) {
        Optional<GroupEntity> group = groupRepository.findById(id);

        if (group.isPresent() && group.get().getGroupItem().equals(groupItem)) {
            groupRepository.delete(group.get());
            return true;
        }
        return false;
    }

    // 모든 그룹 목록 조회
    public List<GroupEntity> getAllGroups() {
        return groupRepository.findAll();
    }
}

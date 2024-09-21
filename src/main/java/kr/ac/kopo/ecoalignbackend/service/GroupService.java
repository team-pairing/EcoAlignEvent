package kr.ac.kopo.ecoalignbackend.service;

import kr.ac.kopo.ecoalignbackend.entity.GroupEntity;

import java.util.List;

public interface GroupService {

    // 그룹 추가
    void addGroup(String groupItem);
    // 그룹 삭제
    boolean deleteGroup(String id, String groupItem);
    // 모든 그룹 목록 조회
    List<GroupEntity> getAllGroups();
}

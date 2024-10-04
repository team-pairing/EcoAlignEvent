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
    // 토큰 검증
    boolean validateAuth(String token);
    // 그룹 중복 확인
    boolean checkGroupItem(String groupItem);
    // 헤더에서 아이디 식별
    String getMemberId(String token);
}

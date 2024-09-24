package kr.ac.kopo.ecoalignbackend.service;

import kr.ac.kopo.ecoalignbackend.dto.MemoDTO;
import kr.ac.kopo.ecoalignbackend.entity.MemoEntity;

import java.util.List;
import java.util.Map;

public interface MemoService {
    // 메모 추가
    void addMemo(Map<String, Object> request);

    // 메모 수정
    boolean updateMemo(MemoDTO memoDTO);

    // 메모 삭제
    void deleteMemo(String memoId);

    // 메모 조회
    List<MemoEntity> allMemo();

    // 토큰 검증
    boolean validateAuth(String token);
}

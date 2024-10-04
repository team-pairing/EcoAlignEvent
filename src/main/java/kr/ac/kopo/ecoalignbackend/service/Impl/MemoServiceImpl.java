package kr.ac.kopo.ecoalignbackend.service.Impl;

import kr.ac.kopo.ecoalignbackend.dto.MemoDTO;
import kr.ac.kopo.ecoalignbackend.entity.MemoEntity;
import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
import kr.ac.kopo.ecoalignbackend.repository.MemoRepository;
import kr.ac.kopo.ecoalignbackend.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemoServiceImpl implements MemoService {
    private final MemoRepository memoRepository;
    private final JwtUtil jwtUtil;
    // 메모 추가
    public void addMemo(Map<String, Object> request){
        String title = (String) request.get("title");
        String content = (String) request.get("content");
        String date = (String) request.get("date");

        MemoEntity memoEntity = new MemoEntity();
        memoEntity.setId();
        memoEntity.setTitle(title);
        memoEntity.setContent(content);
        memoEntity.setDate(date);

        memoRepository.save(memoEntity);
    }

    // 메모 수정
    public boolean updateMemo(MemoDTO memoDTO) {
        if (memoRepository.findById(memoDTO.getId()).isPresent()) {
            MemoEntity memoEntity = memoRepository.findById(memoDTO.getId()).get();
            if (!memoEntity.getTitle().equals(memoDTO.getTitle())) {
                memoEntity.setTitle(memoDTO.getTitle());
                memoRepository.save(memoEntity);
            }
            if (!memoEntity.getContent().equals(memoDTO.getContent())) {
                memoEntity.setContent(memoDTO.getContent());
                memoRepository.save(memoEntity);
            }
            return true;
        } else return false;
    }

    // 메모 삭제
    public void deleteMemo(String memoId) {
        if (memoRepository.findById(memoId).isPresent()) {
            MemoEntity memoEntity = memoRepository.findById(memoId).get();
            memoRepository.delete(memoEntity);
        }
    }

    // 메모 조회
    public List<MemoEntity> allMemo() {
        return memoRepository.findAll();
    }

    // 토큰 검증
    public boolean validateAuth(String token){
        token = jwtUtil.tokenSorting(token);
        return jwtUtil.validateToken(token);
    }
}


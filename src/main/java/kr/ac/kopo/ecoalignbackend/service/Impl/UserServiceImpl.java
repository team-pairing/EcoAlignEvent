package kr.ac.kopo.ecoalignbackend.service.Impl;

import kr.ac.kopo.ecoalignbackend.dto.UserDTO;
import kr.ac.kopo.ecoalignbackend.entity.UserEntity;
import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
import kr.ac.kopo.ecoalignbackend.jwt.Token;
import kr.ac.kopo.ecoalignbackend.repository.GroupRepository;
import kr.ac.kopo.ecoalignbackend.repository.MemoRepository;
import kr.ac.kopo.ecoalignbackend.repository.ScheduleRepository;
import kr.ac.kopo.ecoalignbackend.repository.UserRepository;
import kr.ac.kopo.ecoalignbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final MemoRepository memoRepository;
    private final ScheduleRepository scheduleRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원가입
    public UserDTO registerUser(Map<String, Object> requestUser){
        // birth 또는 gender를 입력하지 않았을 경우 default 값으로 설정
        requestUser.putIfAbsent("birth", "19000101");
        requestUser.putIfAbsent("gender", "male");

        // 입력받은 json 객체로 사용자 생성
        UserDTO userDTO = new UserDTO();
        userDTO.setMemberId((String)requestUser.get("memberId"));
        userDTO.setPassword((String)requestUser.get("password"));
        userDTO.setEmail((String)requestUser.get("email"));
        userDTO.setName((String)requestUser.get("name"));
        userDTO.setBirth((String)requestUser.get("birth"));
        userDTO.setGender((String)requestUser.get("gender"));

        // 권한부여
        UserEntity entity = dtoToEntity(userDTO);
        UserEntity user = createUserEntity(entity);

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);

        // 사용자 저장
        userRepository.save(user);

        // 생성된 사용자 객체 반환
        return entityToDto(user);
    }

    // 아이디를 가진 사용자가 존재하는지 확인
    public Optional<UserEntity> findByMemberId(String memberId){
        return userRepository.findByMemberId(memberId);
    }

    // 아이디를 가진 사용자 찾기
    public UserEntity findUserByMemberId(String memberId) {
        return userRepository.findUserByMemberId(memberId);
    }

    // 로그인
    public Token logIn(String memberId, String password){
        UserEntity requestUser = userRepository.findUserByMemberId(memberId);

        // 암호화된 비밀번호와 일치하는지 확인
        String encodedPassword = requestUser.getPassword();
        // 일치할 경우 권한을 확인하고 없으면 USER 권한 부여
        if (passwordEncoder.matches(password, encodedPassword)){
            if (requestUser.getAuthorities().stream().noneMatch(authority -> authority.getAuthority().equals("USER"))){
                requestUser.setAuthority(List.of("USER"));
            }
            // USER 권한이 부여된 토큰 생성
            return jwtUtil.createToken(entityToDto(requestUser));
        } else {
            // 비밀번호가 일치하지 않을 때
            log.info("Wrong password");
            return null;
        }
    }

    // 아이디 찾기
    @Transactional
    public String findMemberIdByNameAndEmailAndBirth(String name, String email, String birth){
        if (userRepository.findByNameAndEmailAndBirth(name, email, birth).isPresent()) {
            UserEntity user = userRepository.findUserByNameAndEmailAndBirth(name, email, birth);
            return user.getMemberId();
        } else {
            return null;
        }
    }

    // 비밀번호 수정할 유저 찾기
    public Optional<UserEntity> findPasswordUser(String memberId, String email){
        return userRepository.findBymemberIdAndEmail(memberId, email);
    }

    // 비밀번호 수정
    public int updatePassword(String memberId, String password){
        UserEntity user = userRepository.findUserByMemberId(memberId);

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        // 사용자 저장
        userRepository.save(user);

        if (user.getPassword().equals(encodedPassword)) {
            return 1;
        } else return 0;
    }

    // 사용자 변경사항 저장 - 회원정보 수정에 필요
    public UserEntity saveUser(UserEntity user){
        return userRepository.save(user);
    }

    // 사용자 생성
    public UserEntity createUserEntity(UserEntity userEntity){
        // 권한 부여
        Collection<? extends GrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("USER"));

        List<String> authorityList = authorities.stream()
                .map(GrantedAuthority::getAuthority) // 각 GrantedAuthority에서 권한 문자열 추출
                .collect(Collectors.toList());

        userEntity.setAuthority(authorityList);

        return userEntity;
    }

    // 사용자 삭제
    @Transactional
    public int deleteUserEntity(String memberId, String password){
        UserEntity requestUser = userRepository.findUserByMemberId(memberId);
        if (userRepository.findByMemberId(memberId).isEmpty()) {
            return -1; // 아이디가 없는 경우
        } else {
            // 암호화된 비밀번호와 일치하는지 확인
            String encodedPassword = requestUser.getPassword();
            if (passwordEncoder.matches(password, encodedPassword)){
                memoRepository.deleteAllByMemberId(memberId);
                scheduleRepository.deleteAllByMemberId(memberId);
                groupRepository.deleteAllByMemberId(memberId);
                userRepository.deleteUserEntityByMemberId(memberId);
                return 1; // 비밀번호가 일치하는 경우
            } else {
                return 0; // 비밀번호가 틀린 경우
            }
        }
    }

    // JWT 인증 사용자 찾기
    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByMemberId(username)
                .map(this::createUserEntity)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }
}

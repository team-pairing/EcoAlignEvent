//package kr.ac.kopo.ecoalignbackend.service.Impl;
//
//import jakarta.transaction.Transactional;
//import kr.ac.kopo.ecoalignbackend.entity.User;
//import kr.ac.kopo.ecoalignbackend.jwt.JwtUtil;
//import kr.ac.kopo.ecoalignbackend.repository.UserRepository;
//import kr.ac.kopo.ecoalignbackend.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//    private final BCryptPasswordEncoder passwordEncoder;
//    private final ModelMapper modelMapper;
//
//    // 회원가입
//    @Override
//    public User registerUser(){
//        return null;
//    }
//
//    // 로그인
//    @Override
//    public String logIn(){
//        return null;
//    }
//
//
//    // 아이디와 비밀번호로 사용자 찾기
//    @Override
//    public Optional<User> findUserByMemberIdAndPassword(){
//        return Optional.empty();
//    }
//
//    // 아이디 찾기
//    @Override
//    public Optional<User> findMemberIdbyNameAndEmail(){
//        return Optional.empty();
//    }
//
//    // 비밀번호 수정할 사용자 찾기
//    @Override
//    public Optional<User> findPasswordUser(){
//        return Optional.empty();
//    }
//
//    // 비밀번호 수정
////    public
//
//    // 아이디로 사용자 조회 - 회원정보 수정에 필요
//    @Override
//    public Optional<User> findById(String id){
//        return userRepository.findUserById(id);
//    }
//
//    // 사용자 변경사항 저장 - 회원정보 수정에 필요
//    @Override
//    @Transactional
//    public User saveUser(User user) {
//        return userRepository.save(user);
//    }
//}

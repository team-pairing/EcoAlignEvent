package kr.ac.kopo.ecoalignbackend.service.details;

import kr.ac.kopo.ecoalignbackend.dto.CustomUserInfoDTO;
import kr.ac.kopo.ecoalignbackend.entity.User;
import kr.ac.kopo.ecoalignbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public CustomUserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        User user = userRepository.findByMemberId(memberId);
        // 사용자가 없으면 예외처리
        if (user == null){
            throw new UsernameNotFoundException("No existing user.");
        }

        CustomUserInfoDTO customUserInfoDTO = modelMapper.map(user, CustomUserInfoDTO.class);

        return new CustomUserDetails(customUserInfoDTO);
    }
}

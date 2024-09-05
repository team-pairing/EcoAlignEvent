package kr.ac.kopo.ecoalignbackend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDTO {
        private String memberId;
        private String password;
}

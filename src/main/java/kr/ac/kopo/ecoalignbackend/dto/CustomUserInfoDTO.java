package kr.ac.kopo.ecoalignbackend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomUserInfoDTO {
    private String memberId;

    private String email;

    private String name;

    private String password;

    private RoleType role;

}

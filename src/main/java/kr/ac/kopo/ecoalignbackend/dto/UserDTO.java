package kr.ac.kopo.ecoalignbackend.dto;

import kr.ac.kopo.ecoalignbackend.RoleType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String memberId;
    private String password;
    private String email;
    private String name;
    private String birth;
    private String gender;
    private RoleType role;
}

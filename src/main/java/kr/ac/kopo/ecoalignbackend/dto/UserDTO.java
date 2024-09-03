package kr.ac.kopo.ecoalignbackend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String member_id;
    private String password;
    private String email;
    private String name;
    private String birth;
    private String gender;
}

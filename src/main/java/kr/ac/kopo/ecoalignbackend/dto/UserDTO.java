package kr.ac.kopo.ecoalignbackend.dto;

import lombok.*;

import java.util.List;
import java.util.Random;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String memberId;
    private String password;
    private String email;
    private String name;
    private String birth;
    private String gender;
    private List<String> authority;
}

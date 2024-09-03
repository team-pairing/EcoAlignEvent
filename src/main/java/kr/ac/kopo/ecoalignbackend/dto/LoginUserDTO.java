package kr.ac.kopo.ecoalignbackend.dto;

import lombok.Data;

@Data
public class LoginUserDTO extends UserDTO {
        private String member_id;
        private String password;
}

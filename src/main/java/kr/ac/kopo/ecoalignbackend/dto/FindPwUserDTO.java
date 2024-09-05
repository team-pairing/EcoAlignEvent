package kr.ac.kopo.ecoalignbackend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindPwUserDTO{
    private String memberId;
    private String email;
}

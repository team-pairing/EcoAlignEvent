package kr.ac.kopo.ecoalignbackend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindIdUserDTO{
    private String name;
    private String email;
    private String birth;
}

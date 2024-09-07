package kr.ac.kopo.ecoalignbackend.dto;

import lombok.*;

import java.util.List;
import java.util.Random;

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
    private List<String> authority;

    public String idSetting(){
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) { // 아이디는 6자리
            int index = random.nextInt(3); // 0~2까지 랜덤, 랜덤값으로 switch문 실행

            switch (index) {
                case 0 -> code.append((char) (random.nextInt(26) + 97)); // 소문자
                case 1 -> code.append((char) (random.nextInt(26) + 65)); // 대문자
                case 2 -> code.append(random.nextInt(10)); // 숫자
            }
        }
        return code.toString();
    }
}

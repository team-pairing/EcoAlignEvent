package kr.ac.kopo.ecoalignbackend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Random;

@Entity
@Table(name = "memo")
@Data
public class MemoEntity {
    @Id
    @Column(nullable = false)
    @NotNull
    private String id;

    @Column(nullable = false)
    @NotNull
    private String title;

    @Column
    private String content;

    @Column
    private String Date;

    public void setId() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) { // id 4자리
            int index = random.nextInt(3); // 0~2까지 랜덤, 랜덤값으로 switch문 실행

            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 97)); // 소문자
                case 1 -> key.append((char) (random.nextInt(26) + 65)); // 대문자
                case 2 -> key.append(random.nextInt(10)); // 숫자
            }
        }
        this.id = key.toString();
    }
}


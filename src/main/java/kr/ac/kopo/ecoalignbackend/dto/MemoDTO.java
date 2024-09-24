package kr.ac.kopo.ecoalignbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemoDTO {
    private String id;
    private String title;
    private String content;
}

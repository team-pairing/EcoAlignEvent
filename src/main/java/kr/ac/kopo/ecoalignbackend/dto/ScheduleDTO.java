package kr.ac.kopo.ecoalignbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {
    private String id;
    private String color;
    private String details;
    private String end;
    private String kind;
    private String name;
    private String start;
    private boolean timed;
}

package kr.ac.kopo.ecoalignbackend.dto;

import lombok.*;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {
    private String id;
    private String name;
    private String details;
    private String color;
    private String kind;
    private Date start;
    private Date end;
    private boolean timed;
}

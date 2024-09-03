package kr.ac.kopo.ecoalignbackend.entity;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "schedule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    private String id;

    @Column
    private String name;

    @Column
    private String details;

    @Column
    private String color;

    @Column
    private String kind;

    @Column
    private Date start;

    @Column
    private Date end;

    @Column
    private boolean timed;

    @PrePersist
    public void generateId(){
        if (id.equals(null)){
            id = UUID.randomUUID().toString();
        }
    }
}

package kr.ac.kopo.ecoalignbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;

    @Column(name = "member_id", length = 20, nullable = false)
    private String member_id;

    @Column(name = "password", length = 20, nullable = false)
    private String password;

    @Column(name = "email", length = 30, nullable = false, unique = true)
    private String email;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "birth")
    private String birth;

    @Column(name = "gender")
    private String gender;

    @PrePersist
    public void generateId(){
        if (id.equals(null)){
            id = UUID.randomUUID().toString();
        }
    }
}
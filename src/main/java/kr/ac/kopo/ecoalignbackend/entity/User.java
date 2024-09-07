//package kr.ac.kopo.ecoalignbackend.entity;
//import jakarta.persistence.*;
//import kr.ac.kopo.ecoalignbackend.RoleType;
//import lombok.*;
//
//import java.util.UUID;
//
//@Entity
//@Table(name = "user")
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class User {
//
//    @Id
//    private String id;
//
//    @Column(name="member_id", length = 20, nullable = false)
//    private String memberId;
//
//    @Column(length = 20, nullable = false)
//    private String password;
//
//    @Column(length = 30, nullable = false, unique = true)
//    private String email;
//
//    @Column(length = 20, nullable = false)
//    private String name;
//
//    @Column
//    private String birth;
//
//    @Column
//    private String gender;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "ROLE", nullable = false)
//    private RoleType role;
//
//    @PrePersist
//    public void generateId(){
//        id = UUID.randomUUID().toString();
//    }
//}

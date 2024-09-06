package kr.ac.kopo.ecoalignbackend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
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

    @Column(name="member_id", length = 20, nullable = false)
    private String memberId;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 30, nullable = false, unique = true)
    private String email;

    @Column(length = 20, nullable = false)
    private String name;

    @Column
    private String birth;

    @Column
    private String gender;

    @ManyToMany(fetch = FetchType.EAGER) // FetchType.EAGER를 사용하여 권한을 즉시 로드
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @PrePersist
    public void generateId(){
        id = UUID.randomUUID().toString();
    }
}

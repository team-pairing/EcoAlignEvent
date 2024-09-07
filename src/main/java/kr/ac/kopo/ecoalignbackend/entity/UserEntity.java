//package kr.ac.kopo.ecoalignbackend.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import jakarta.validation.constraints.NotNull;
//import lombok.Builder;
//import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.UUID;
//
//@Entity
//@Table(name = "user")
//@Data
//@Builder
//public class UserEntity implements UserDetails {
//    @Id
//    private String id;
//
//    @Column
//    @NotNull
//    private String memberId;
//
//    @Column
//    @NotNull
//    private String password;
//
//    @Column
//    @NotNull
//    private String email;
//
//    @Column
//    @NotNull
//    private String name;
//
//    @Column
//    private String birth;
//
//    @Column
//    private String gender;
//
//    @Column
//    private GrantedAuthority authorities;
//
//    public UserEntity() {
////        this.id = UUID.randomUUID().toString();
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return memberId;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}

package com.okatu.rgan.user.model;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

// about the difference between lock and enable, see
// https://stackoverflow.com/questions/1611365/spring-security-what-is-the-difference-between-account-locked-and-not-enabled
// 比方说，一个账户被locked，可能是因为在短时间内尝试了若干次错误的凭证登陆失败
// 而disabled，可能是因为某个管理员的手动操作
@Entity
@Table(name = "user")
public class RganUser implements UserDetails {
    private static int ACTIVE = 1;

    private static int LOCKED = 2;

    private static int DISABLED = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private Integer status = ACTIVE;

    private String email;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", updatable = false, insertable = false,
        columnDefinition = "datetime default CURRENT_TIMESTAMP")
    @Generated(value = GenerationTime.ALWAYS)
    private LocalDateTime createdTime;

    @Transient
    private Set<GrantedAuthority> grantedAuthorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package com.okatu.rgan.user.model;

import com.okatu.rgan.user.authentication.constant.UserAccountStatus;
import com.okatu.rgan.user.authentication.constant.UserVerificationStatus;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // https://stackoverflow.com/questions/2593722/hibernate-one-to-one-getid-without-fetching-entire-object
    // https://stackoverflow.com/questions/32220951/just-getting-id-column-value-not-using-join-in-hibernate-object-one-to-many-rela
    private Long id;

    private String username;

    private String password;

    private Integer status = UserAccountStatus.ACTIVE;

    private String email;

////    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "created_time", updatable = false, insertable = false,
//        columnDefinition = "datetime default CURRENT_TIMESTAMP")
//    @Generated(value = GenerationTime.ALWAYS)
    private LocalDateTime createdTime;

    @PrePersist
    private void prePersist(){
        createdTime = LocalDateTime.now();
    }

    private LocalDateTime lastLoginTime;

    private String verificationEmail;

    private String verificationToken;

    @Column(nullable = false)
    private Integer verificationStatus = UserVerificationStatus.CREATED;

    private LocalDateTime verificationCreatedTime;

    private String description;

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
        return status != UserAccountStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserAccountStatus.ACTIVE;
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

    public String getVerificationEmail() {
        return verificationEmail;
    }

    public void setVerificationEmail(String verificationEmail) {
        this.verificationEmail = verificationEmail;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Integer getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(Integer verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public LocalDateTime getVerificationCreatedTime() {
        return verificationCreatedTime;
    }

    public void setVerificationCreatedTime(LocalDateTime verificationCreatedTime) {
        this.verificationCreatedTime = verificationCreatedTime;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static boolean isNotSame(RganUser lhs, RganUser rhs){
        return (lhs == null || !lhs.getId().equals(rhs.getId())) && (lhs != rhs);
    }
}

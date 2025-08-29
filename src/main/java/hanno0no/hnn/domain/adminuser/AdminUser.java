package hanno0no.hnn.domain.adminuser;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Table(name = "admin_user")
public class AdminUser implements UserDetails {

    @Id                 // 바로 아래 필드인 id가 기본키라는 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private int adminId;

    @Column(name = "username")
    private String userName;

    private String password_hash;
    private String role;

    public AdminUser() {}  // 기본 생성자

    public int getAdminId() {
        return adminId;
    }
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword_hash() {
        return password_hash;
    }
    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 이 사용자가 가진 권한(Role) 목록을 반환합니다.
        // role 필드의 값을 기반으로 "ROLE_" 접두사를 붙여서 권한 객체를 만듭니다.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getPassword() {
        return this.password_hash;
    }

    @Override
    public String getUsername() {
        // Spring Security에서는 username을 식별자로 사용합니다.
        // 우리 엔티티의 username 필드를 반환합니다.
        return this.userName;
    }

    // 아래는 계정 상태에 대한 메소드들입니다. 지금은 간단히 모두 true로 설정합니다.
    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않았는가?
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않았는가?
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호가 만료되지 않았는가?
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화되었는가?
    }

}

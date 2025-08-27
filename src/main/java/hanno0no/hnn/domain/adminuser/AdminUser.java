package hanno0no.hnn.domain.adminuser;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_user")
public class AdminUser {

    @Id                 // 바로 아래 필드인 id가 기본키라는 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private int adminId;

    @Column(name = "username")
    private String userName;

    private String password;
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

}

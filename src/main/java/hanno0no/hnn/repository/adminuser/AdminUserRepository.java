package hanno0no.hnn.repository.adminuser;

import hanno0no.hnn.domain.adminuser.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Integer> {
    Optional<AdminUser> findByUserName(String username);        // username으로 사용자 조회

    boolean existsByUserName(String username);      // username 존재 여부 확인

    List<AdminUser> findByRole(String role);        // 특정 역할을 가진 관리자 리스트


}

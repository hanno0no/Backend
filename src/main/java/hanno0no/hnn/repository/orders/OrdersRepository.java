package hanno0no.hnn.repository.orders;


import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.domain.state.State;
import hanno0no.hnn.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    @Query("SELECT o FROM Orders o WHERE o.state.stateId = :stateId")
    List<Orders> findOrdersByStateId(@Param("stateId") Integer stateId);        // 해당 상태번호에 대한 전체 주문건을 반환함
    // Integer를 쓰는 이유 : 객체로 반환이 가능하고, null이 가능함.

    // stateId가 주어진 List에 포함되지 않는(NotIn) 모든 Orders를 조회
//    List<Orders> findByState_StateIdNotIn(List<Integer> stateIds);

    @Query("SELECT o FROM Orders o WHERE o.state.stateId NOT IN :stateNums ORDER BY o.orderedAt ASC")
    List<Orders> findByState_StateIdNotIn(@Param("stateNums") List<Integer> stateNums);

    @Query("SELECT o FROM Orders o WHERE o.team.teamNum = :team")
    List<Orders> findByTeam(@Param("team") String team);         // team에 대한 모든 주문건 조회


    @Query("SELECT o FROM Orders o WHERE o.admin.userName = :username")
    List<Orders> findByAdmin(@Param("username") String username);

    @Query("select o from Orders o where o.admin.userName = :username and o.state.stateId = :stateId")
    List<Orders> findByAdminAndStateId(@Param("username") String username, @Param("stateId") Integer stateId);


    @Query("SELECT o FROM Orders o WHERE o.state.stateId = :stateNum ORDER BY o.updatedAt DESC")
    List<Orders> findTop9CompletedOrders(@Param("stateNum") Integer stateNum, Pageable pageable);

    @Query("SELECT o FROM Orders o WHERE o.state.stateId NOT IN :stateNums ORDER BY o.orderedAt ASC")
    List<Orders> findOldestOngoingOrders(@Param("stateNums") List<Integer> stateNums, Pageable pageable);
}

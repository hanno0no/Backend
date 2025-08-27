package hanno0no.hnn.repository.orders;


import hanno0no.hnn.domain.orders.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    @Query("SELECT o FROM Orders o where o.stateId = :stateId")
    List<Orders> findOrdersByStateId(Integer stateId);      // 해당 상태번호에 대한 전체 주문건을 반환함
    // Integer를 쓰는 이유 : 객체로 반환이 가능하고, null이 가능함.

    // stateId가 주어진 List에 포함되지 않는(NotIn) 모든 Orders를 조회
    List<Orders> findByStateIdNotIn(List<Integer> stateIds);



}

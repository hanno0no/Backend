package hanno0no.hnn.repository.state;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StateRepository extends JpaRepository {

    List<StateRepository> findAllStates();    // 상태에 대한 전체를 리스트로 반환

    @Query("SELECT s.stateId FROM State s WHERE s.state = :state")
    Integer findStateIdByState(String state);           // 상태에 대한 상태ID 반환하기

}

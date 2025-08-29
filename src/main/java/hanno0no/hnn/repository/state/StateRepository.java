package hanno0no.hnn.repository.state;

import hanno0no.hnn.domain.state.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StateRepository extends JpaRepository<State, Integer> {

    List<State> findAll();    // 상태에 대한 전체를 리스트로 반환

    @Query("SELECT s.stateId FROM State s WHERE s.state = :stateName")
    Optional<Integer> findStateIdByState(@Param("stateName") String stateName);           // 상태에 대한 상태ID 반환하기

    @Query("SELECT s.state from  State s WHERE s.stateId = :stateId")
    Optional<State> findStateById(@Param("stateId") Integer stateId);


}

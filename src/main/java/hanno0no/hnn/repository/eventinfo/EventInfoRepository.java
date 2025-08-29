package hanno0no.hnn.repository.eventinfo;

import hanno0no.hnn.domain.eventinfo.EventInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventInfoRepository extends JpaRepository<EventInfo, Integer> {

    List<EventInfo> findAll();          // 모든 행사들 리스트로 반환

    Optional<EventInfo> findEventInfoByEventName(String eventName);

    @Query("SELECT e.endTime FROM EventInfo e WHERE e.eventId = :eventId")
    Optional<LocalDateTime> findEndTimeByEventId(@Param("eventId") Integer eventId);        // 행사가 끝나는 시간 로딩

    @Query("SELECT e from EventInfo e where e.isOpen = true ")
    Optional<EventInfo> findByIsOpen();

    @Query("SELECT e from EventInfo e where e.isOpen = false ")
    List<EventInfo> findAllByIsNotOpen();

}

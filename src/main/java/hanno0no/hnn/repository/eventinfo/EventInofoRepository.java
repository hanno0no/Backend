package hanno0no.hnn.repository.eventinfo;

import hanno0no.hnn.domain.eventinfo.EventInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventInofoRepository extends JpaRepository<EventInfo, Integer> {

    List<EventInfo> findAll();          // 모든 행사들 리스트로 반환

    LocalDateTime findEndTimeByEventId(Integer eventId);        // 행사가 끝나는 시간 로딩


}

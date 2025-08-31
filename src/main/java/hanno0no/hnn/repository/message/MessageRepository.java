package hanno0no.hnn.repository.message;

import hanno0no.hnn.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE m.emergency = true and m.display = true")
    List<Message> findAllByEmergency();         // 중요공지들 리턴 + 보여줄 것들


//    List<Message> findAllByEmergencyAndDisplay(boolean emergency, boolean display);


    @Query("SELECT m FROM Message m WHERE m.emergency = false and m.display = true")
    List<Message> findAllByNonEmergency();         // 일반 공지들 리턴 + 보여줄 것들



}

package hanno0no.hnn.repository.state;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface State extends JpaRepository {

    List<State> findAll();

    List<State> findbyState (String state);



}

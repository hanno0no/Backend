package hanno0no.hnn.repository.team;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Team extends JpaRepository<Team, String> {

    Optional<Team> findByTeamNum(String teamNum);

    boolean existsByTeamNum(String teamNum);

    List<Team> findAll();
}


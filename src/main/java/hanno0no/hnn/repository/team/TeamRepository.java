package hanno0no.hnn.repository.team;

import hanno0no.hnn.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

    Optional<Team> findByTeamNum(String teamNum);

    boolean existsByTeamNum(String teamNum);

    List<Team> findAll();
}


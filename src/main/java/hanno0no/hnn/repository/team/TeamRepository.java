package hanno0no.hnn.repository.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TeamRepository extends JpaRepository<TeamRepository, String> {

    Optional<TeamRepository> findByTeamNum(String teamNum);

    boolean existsByTeamNum(String teamNum);

    List<TeamRepository> findAll();
}


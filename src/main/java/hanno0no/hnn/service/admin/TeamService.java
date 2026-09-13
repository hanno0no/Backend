package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.team.Team;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.team.TeamRepository;
import hanno0no.hnn.request.admin.TeamCreateRequest;
import hanno0no.hnn.request.admin.TeamUpdateRequest;
import hanno0no.hnn.response.admin.TeamResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final OrdersRepository ordersRepository;

    public List<TeamResponse> getTeams() {
        return teamRepository.findAll().stream()
                .map(team -> new TeamResponse(team.getTeamNum(), team.getPhoneNumber()))
                .toList();
    }

    @Transactional
    public void createTeam(TeamCreateRequest request) {
        if (!StringUtils.hasText(request.getTeamNum())) {
            throw new IllegalArgumentException("팀 번호는 필수입니다.");
        }
        if (teamRepository.existsByTeamNum(request.getTeamNum())) {
            throw new IllegalArgumentException("이미 등록된 팀 번호입니다: " + request.getTeamNum());
        }

        Team team = new Team();
        team.setTeamNum(request.getTeamNum());
        team.setPhoneNumber(StringUtils.hasText(request.getPhoneNumber()) ? request.getPhoneNumber() : null);
        teamRepository.save(team);
    }

    @Transactional
    public void updateTeam(String teamNum, TeamUpdateRequest request) {
        Team team = teamRepository.findByTeamNum(teamNum)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다: " + teamNum));

        team.setPhoneNumber(StringUtils.hasText(request.getPhoneNumber()) ? request.getPhoneNumber() : null);
    }

    @Transactional
    public void deleteTeam(String teamNum) {
        Team team = teamRepository.findByTeamNum(teamNum)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다: " + teamNum));

        if (!ordersRepository.findByTeam(teamNum).isEmpty()) {
            throw new IllegalStateException("접수 내역이 있는 팀은 삭제할 수 없습니다: " + teamNum);
        }

        teamRepository.delete(team);
    }
}

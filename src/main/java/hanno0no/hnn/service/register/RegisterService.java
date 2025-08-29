package hanno0no.hnn.service.register;


import hanno0no.hnn.domain.material.Material;
import hanno0no.hnn.domain.orders.Orders;
import hanno0no.hnn.domain.state.State;
import hanno0no.hnn.domain.team.Team;
import hanno0no.hnn.repository.material.MaterialRepository;
import hanno0no.hnn.repository.orders.OrdersRepository;
import hanno0no.hnn.repository.state.StateRepository;
import hanno0no.hnn.repository.team.TeamRepository;
import hanno0no.hnn.request.register.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final OrdersRepository ordersRepository;
    private final TeamRepository teamRepository;
    private final MaterialRepository materialRepository;
    private final StateRepository stateRepository;

    @Transactional // ✨ 데이터를 DB에 쓰는 작업은 반드시 @Transactional을 붙여야 합니다.
    public int createOrder(RegisterRequest requestDto) {
        // 1. 요청 DTO에 담긴 이름/번호 등으로 실제 연관 엔티티들을 조회합니다.
        Team team = teamRepository.findByTeamNum(requestDto.getTeamNum())
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        Material material = materialRepository.findByMaterialName(requestDto.getMaterial())
                .orElseThrow(() -> new IllegalArgumentException("재질을 찾을 수 없습니다."));

        State initialState = stateRepository.findByState("submission") // 'submission' 상태를 DB에서 조회
                .orElseThrow(() -> new IllegalStateException("초기 상태(submission)를 찾을 수 없습니다."));

        Orders newOrder = new Orders();

        newOrder.setMaterial(material);
        newOrder.setState(initialState);
        newOrder.setTeam(team);

        Orders savedOrder = ordersRepository.save(newOrder);

        String teamName = savedOrder.getTeam().getTeamNum();
        int orderId = savedOrder.getOrderId();
        String materialCode = getMaterialCode(savedOrder.getMaterial().getMaterialName());

        String fileName = String.format("%s_%d%s", teamName, orderId, materialCode);

        savedOrder.setFileName(fileName);

        return savedOrder.getOrderId();

    }

    private String getMaterialCode(String materialName) {
        // 1. 안전한 비교를 위해, 입력받은 materialName이 null인지 먼저 확인합니다.
        if (materialName == null) {
            return "";
        }

        // 2. 대소문자 구분 없는 비교를 위해 입력받은 문자열을 모두 대문자로 변경합니다.
        String upperCaseMaterialName = materialName.toUpperCase();

        // 3. equalsIgnoreCase 대신 contains를 사용하여 '아크릴' 또는 'MDF' 문자열이 포함되어 있는지 확인합니다.
        if (upperCaseMaterialName.contains("아크릴")) {
            return "A";
        } else if (upperCaseMaterialName.contains("MDF")) {
            return "M";
        }

        return "";
    }

}

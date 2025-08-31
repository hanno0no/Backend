package hanno0no.hnn.service.admin;

import hanno0no.hnn.repository.eventinfo.EventInfoRepository;
import hanno0no.hnn.repository.material.MaterialRepository;
import hanno0no.hnn.repository.message.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSettingService {

    private final EventInfoRepository eventInfoRepository;
    private final MessageRepository messageRepository;
    private final MaterialRepository materialRepository;

    @Transactional
    public void deleteEventInfo(Integer eventId) {
        // ID로 엔티티가 존재하는지 먼저 확인합니다. 없으면 예외가 발생합니다.
        if (!eventInfoRepository.existsById(eventId)) {
            throw new IllegalArgumentException("삭제하려는 이벤트 정보를 찾을 수 없습니다. ID: " + eventId);
        }
        eventInfoRepository.deleteById(eventId);
    }

    @Transactional
    public void deleteMessage(Integer messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new IllegalArgumentException("삭제하려는 메시지를 찾을 수 없습니다. ID: " + messageId);
        }
        messageRepository.deleteById(messageId);
    }

    @Transactional
    public void deleteMaterial(Integer materialId) {
        // 재질(Material)은 다른 주문(Orders)에서 참조하고 있을 수 있습니다.
        // 만약 참조하는 주문이 있는데 재질을 삭제하려고 하면 Foreign Key 제약 조건 위배로 DB 에러가 발생할 수 있습니다.
        // 실제 운영 환경에서는 해당 재질을 사용하는 주문이 있는지 확인하는 로직을 추가하는 것이 안전합니다.
        if (!materialRepository.existsById(materialId)) {
            throw new IllegalArgumentException("삭제하려는 재질 정보를 찾을 수 없습니다. ID: " + materialId);
        }
        materialRepository.deleteById(materialId);
    }
}
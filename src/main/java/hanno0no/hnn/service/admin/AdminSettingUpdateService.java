package hanno0no.hnn.service.admin;


import hanno0no.hnn.domain.eventinfo.EventInfo;
import hanno0no.hnn.domain.material.Material;
import hanno0no.hnn.domain.message.Message;
import hanno0no.hnn.repository.eventinfo.EventInfoRepository;
import hanno0no.hnn.repository.material.MaterialRepository;
import hanno0no.hnn.repository.message.MessageRepository;
import hanno0no.hnn.request.admin.AdminSettingRequest;
import hanno0no.hnn.request.admin.EventInfoRequestDto;
import hanno0no.hnn.request.admin.MaterialRequestDto;
import hanno0no.hnn.request.admin.MessageRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSettingUpdateService {

    private final EventInfoRepository eventInfoRepository;
    private final MessageRepository messageRepository;
    private final MaterialRepository materialRepository;


    @Transactional
    public void updateAllSettings(AdminSettingRequest request) {


        if (request.getEventInfoRequestDtos() != null) {
            for (EventInfoRequestDto dto : request.getEventInfoRequestDtos()) {
                EventInfo entity = eventInfoRepository.findById(dto.getEventId())
                        .orElseThrow(() -> new IllegalArgumentException("이벤트 정보를 찾을 수 없습니다."));

                if (dto.getEventName() != null) entity.setEventName(dto.getEventName());
                if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
                if (dto.getStartTime() != null) entity.setStartTime(dto.getStartTime());
                if (dto.getEndTime() != null) entity.setEndTime(dto.getEndTime());
                if (dto.getIsOpen() != null) entity.setOpen(dto.getIsOpen()); // DTO의 isOpen()이 Boolean 타입이어야 함

            }
        }

        if (request.getMaterialRequestDtos() != null) {
            for (MaterialRequestDto dto : request.getMaterialRequestDtos()) {
                Material entity = materialRepository.findById(dto.getMaterialId())
                        .orElseThrow(() -> new IllegalArgumentException("재질 정보를 찾을 수 없습니다."));

                if (dto.getMaterialName() != null) entity.setMaterialName(dto.getMaterialName());
                if (dto.getIsActive() != null) entity.setActive(dto.getIsActive());

            }
        }

        if (request.getMessageRequestDtos() != null) {
            for (MessageRequestDto dto : request.getMessageRequestDtos()) {
                Message entity = messageRepository.findById(dto.getMessageId())
                        .orElseThrow(() -> new IllegalArgumentException("메시지 정보를 찾을 수 없습니다."));

                if (dto.getContent() != null) entity.setContent(dto.getContent());
                if (dto.getIsDisplay() != null) entity.setDisplay(dto.getIsDisplay());
                if (dto.getIsEmergency() != null) entity.setEmergency(dto.getIsEmergency());
            }
        }


    }

}

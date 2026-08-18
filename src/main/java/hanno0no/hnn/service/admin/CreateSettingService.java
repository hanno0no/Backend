package hanno0no.hnn.service.admin;


import hanno0no.hnn.domain.eventinfo.EventInfo;
import hanno0no.hnn.domain.material.Material;
import hanno0no.hnn.domain.message.Message;
import hanno0no.hnn.repository.eventinfo.EventInfoRepository;
import hanno0no.hnn.repository.material.MaterialRepository;
import hanno0no.hnn.repository.message.MessageRepository;
import hanno0no.hnn.request.admin.EventInfoCreateRequest;
import hanno0no.hnn.request.admin.MaterialCreateRequest;
import hanno0no.hnn.request.admin.MessageCreateRequest;
import hanno0no.hnn.service.sse.SseEventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateSettingService {

    private final EventInfoRepository eventInfoRepository;
    private final MessageRepository messageRepository;
    private final MaterialRepository materialRepository;
    private final SseEventService sseEventService;

    @Transactional
    public String createMaterial(MaterialCreateRequest request) {

        if (materialRepository.findByMaterialName(request.getMaterialName()).isPresent()) {
            throw new RuntimeException("Material already exists");
        }

        Material material = new Material();

        material.setMaterialName(request.getMaterialName());
        material.setActive(request.isActive());

        Material savedMaterial = materialRepository.save(material);
        sseEventService.emit(SseEventService.SETTINGS_UPDATED);
        return savedMaterial.getMaterialName();
    }

    @Transactional
    public String createEventInfo(EventInfoCreateRequest request) {

        if (eventInfoRepository.findEventInfoByEventName(request.getEventName()).isPresent()) {
            throw new RuntimeException("Event already exists");
        }

        EventInfo eventInfo = new EventInfo();
        eventInfo.setEventName(request.getEventName());
        eventInfo.setStartTime(request.getStartTime());
        eventInfo.setEndTime(request.getEndTime());
        eventInfo.setDescription(request.getDescription());
        eventInfo.setOpen(request.isOpen());
        eventInfo.setCompletedLimit(9);
        eventInfo.setWaitingLimit(12);

        EventInfo savedEventInfo = eventInfoRepository.save(eventInfo);
        sseEventService.emit(SseEventService.INDEX_UPDATED);
        sseEventService.emit(SseEventService.SETTINGS_UPDATED);
        return savedEventInfo.getEventName();

    }

    @Transactional
    public int createMessage(MessageCreateRequest request) {

        Message message = new Message();

        message.setContent(request.getContent());
        message.setDisplay(request.isDisplay());
        message.setEmergency(request.isEmergency());

        Message savedMessage = messageRepository.save(message);
        sseEventService.emit(SseEventService.INDEX_UPDATED);
        sseEventService.emit(SseEventService.SETTINGS_UPDATED);
        return savedMessage.getMessageId();

    }


}

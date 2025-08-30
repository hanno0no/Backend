package hanno0no.hnn.service.admin;

import hanno0no.hnn.domain.eventinfo.EventInfo;
import hanno0no.hnn.domain.material.Material;
import hanno0no.hnn.domain.message.Message;
import hanno0no.hnn.repository.eventinfo.EventInfoRepository;
import hanno0no.hnn.repository.material.MaterialRepository;
import hanno0no.hnn.repository.message.MessageRepository;
import hanno0no.hnn.response.admin.AdminSettingResponse;
import hanno0no.hnn.response.admin.EventInfoDto;
import hanno0no.hnn.response.admin.MaterialDto;
import hanno0no.hnn.response.admin.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.accept.MappingMediaTypeFileExtensionResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSettingService {

    private final EventInfoRepository eventInfoRepository;
    private final MessageRepository messageRepository;
    private final MaterialRepository materialRepository;

    @Transactional(readOnly = true)
    public AdminSettingResponse getAdminSetting() {
        List<EventInfo> eventInfos = eventInfoRepository.findAll();
        List<Message> messages = messageRepository.findAll();
        List<Material> materials = materialRepository.findAll();

        EventInfo openEventInfo = eventInfoRepository.findByIsOpen()
                .orElseThrow(() -> new RuntimeException("No event info found"));

        EventInfoDto openEventInfoDto = new EventInfoDto(openEventInfo);
        List<EventInfoDto> eventInfoDtos = eventInfos.stream().map(EventInfoDto::new).collect(Collectors.toList());
        List<MessageDto> messageDtos = messages.stream().map(MessageDto::new).collect(Collectors.toList());
        List<MaterialDto> materialDtos = materials.stream().map(MaterialDto::new).collect(Collectors.toList());


        return AdminSettingResponse.builder()
                .eventInfos(eventInfoDtos)
                .messages(messageDtos)
                .materials(materialDtos)
                .openEventInfo(openEventInfoDto)
                .build();


    }


}

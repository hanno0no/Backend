package hanno0no.hnn.request.admin;


import lombok.Getter;

@Getter
public class MessageCreateRequest {

    private String content;
    private boolean Emergency;
    private boolean Display;

}

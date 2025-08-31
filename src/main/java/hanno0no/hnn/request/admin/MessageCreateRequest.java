package hanno0no.hnn.request.admin;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageCreateRequest {

    private String content;
    private boolean emergency;
    private boolean display;

}

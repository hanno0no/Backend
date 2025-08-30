package hanno0no.hnn.request.admin;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MaterialRequestDto {
    private int materialId;
    private String materialName;
    private Boolean isActive;
}

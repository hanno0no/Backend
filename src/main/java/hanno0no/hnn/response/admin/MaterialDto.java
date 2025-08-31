package hanno0no.hnn.response.admin;


import hanno0no.hnn.domain.material.Material;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialDto {

    private int materialId;
    private String materialName;
    private boolean isActive;


    public MaterialDto(Material material) {
        this.materialId = material.getMaterialNum();
        this.materialName = material.getMaterialName();
        this.isActive = material.isActive();
    }


}

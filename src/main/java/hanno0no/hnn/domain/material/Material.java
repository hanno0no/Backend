package hanno0no.hnn.domain.material;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "material")
public class Material {

    @Id
    @Column (name = "material_num")
    private int materialNum;

    @Column(name = "material")
    private String materialName;

    @Column(name = "is_active")
    private boolean isActive;

    public Material() {}

    public int getMaterialNum() {
        return materialNum;
    }
    public void setMaterialNum(int materialNum) {
        this.materialNum = materialNum;
    }

    public String getMaterialName() {
        return materialName;
    }
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}

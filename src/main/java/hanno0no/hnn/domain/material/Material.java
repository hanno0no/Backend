package hanno0no.hnn.domain.material;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "material")
public class Material {

    @Id
    @Column (name = "material_num")
    private int materialNum;

    @Column(name = "material")
    private String materialName;

    @Column(name = "is_active")
    private boolean active;

}

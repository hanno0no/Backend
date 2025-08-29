package hanno0no.hnn.repository.material;

import hanno0no.hnn.domain.material.Material;
import hanno0no.hnn.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    List<Material> findAll();

    @Query("SELECT m.materialNum FROM Material m WHERE m.materialName = :materialName")
    Optional<Integer> getMaterialNum(@Param("materialName") String materialName);

    @Query("select m.materialName FROM  Material m where m.materialNum = :materialId")
    String getMaterialName(@Param("materialId") Integer materialId);

    Optional<Material> findByMaterialName(String materialName);



}

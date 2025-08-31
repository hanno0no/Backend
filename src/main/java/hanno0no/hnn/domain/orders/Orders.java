package hanno0no.hnn.domain.orders;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.domain.material.Material;
import hanno0no.hnn.domain.state.State;
import hanno0no.hnn.domain.team.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Orders {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "ordered_at", insertable = false, updatable = false)
    private LocalDateTime orderedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "team_num")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "material")
    private Material material;

    @ManyToOne
    @JoinColumn(name = "state_num")
    private State state;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminUser admin;

}

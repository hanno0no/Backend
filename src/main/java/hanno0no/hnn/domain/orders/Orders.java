package hanno0no.hnn.domain.orders;

import hanno0no.hnn.domain.adminuser.AdminUser;
import hanno0no.hnn.domain.material.Material;
import hanno0no.hnn.domain.state.State;
import hanno0no.hnn.domain.team.Team;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;

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


    public Orders() {}

    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }

    public Material getMaterial() {
        return material;
    }
    public void setMaterial(Material material) {
        this.material = material;
    }

    public State getStateId() {
        return state;
    }
    public void setStateId(State stateId) {
        this.state = stateId;
    }

    public AdminUser getAdmin() {
        return admin;
    }
    public void setAdmin(AdminUser admin) {
        this.admin = admin;
    }



}

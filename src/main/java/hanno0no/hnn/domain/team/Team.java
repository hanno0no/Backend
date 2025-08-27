package hanno0no.hnn.domain.team;

import jakarta.persistence.*;

@Entity
@Table (name = "team")
public class Team {

    @Id
    @Column(name = "team_num")
    private String teamNum;

    @Column(name = "phone")
    private String phoneNumber;

    public Team() {}

    public String getTeamNum() {
        return teamNum;
    }
    public void setTeamNum(String teamNum) {
        this.teamNum = teamNum;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}

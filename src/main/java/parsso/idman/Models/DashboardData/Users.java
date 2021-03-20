package parsso.idman.Models.DashboardData;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Users {
    int total;
    int active;
    int disabled;
    int locked;

    public Users(int total, int active, int disabled, int locked) {
        this.total = total;
        this.active = active;
        this.disabled = disabled;
        this.locked = locked;
    }
}

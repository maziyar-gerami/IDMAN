package parsso.idman.Models.DashboardData;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Services {
    int total;
    int disabled;
    int enabled;

    public Services(int total, int disabled, int enabled) {
        this.total = total;
        this.disabled = disabled;
        this.enabled = enabled;
    }
}

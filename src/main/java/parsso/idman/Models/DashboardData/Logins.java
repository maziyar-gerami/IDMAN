package parsso.idman.Models.DashboardData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Logins {
    int total;
    int unsuccessful;
    int successful;

    public Logins(int total, int unsuccessful, int successful) {
        this.total = total;
        this.unsuccessful = unsuccessful;
        this.successful = successful;
    }
}

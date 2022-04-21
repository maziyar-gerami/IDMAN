package parsso.idman.models.dashboarddata;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Dashboard {
  private String id;
  private Services services;
  private Logins logins;
  private Users users;

  public Dashboard(Services services, Logins logins, Users users) {
    this.services = services;
    this.logins = logins;
    this.users = users;
  }

  @Setter
  @Getter
  public static class Logins {
    int total;
    int unsuccessful;
    int successful;

    public Logins(int total, int unsuccessful, int successful) {
      this.total = total;
      this.unsuccessful = unsuccessful;
      this.successful = successful;
    }
  }

  @Setter
  @Getter
  public static class Services {
    int total;
    int disabled;
    int enabled;

    public Services(int total, int disabled, int enabled) {
      this.total = total;
      this.disabled = disabled;
      this.enabled = enabled;
    }
  }

  @Setter
  @Getter
  public static class Users {
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
}

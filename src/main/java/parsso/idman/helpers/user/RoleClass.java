package parsso.idman.helpers.user;

import parsso.idman.models.users.Role;

public class RoleClass {

  public static Role getRoleClass(String role) {
    int _id = 0;
    switch (role) {
      case "SUPERUSER":
        _id = 0;
        break;
      case "SUPPORTER":
        _id = 1;
        break;
      case "ADMIN":
        _id = 2;
        break;
      case "PRESENTER":
        _id = 3;
        break;
      case "USER":
        _id = 4;
        break;
      default:
        _id = 4;
    }
    return new Role(_id, role);
  }

  public static Role getRoleId(int id) {
    String role = null;
    switch (id) {
      case 0:
        role = "SUPERUSER";
        break;
      case 1:
        role = "SUPPORTER";
        break;
      case 2:
        role = "ADMIN";
        break;
      case 3:
        role = "PRESENTER";
        break;
      case 4:
        role = "USER";
        break;

    }
    return new Role(id, role);
  }

}

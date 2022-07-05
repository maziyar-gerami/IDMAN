package parsso.idman.helpers.onetime;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.RoleClass;
import parsso.idman.models.other.OneTime;

import java.util.Date;
import java.util.List;

public class RoleFix {
  final MongoTemplate mongoTemplate;

  @Autowired
  RoleFix(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public void run() {
    int c = 0;
    char[] animationChars = new char[] { '|', '/', '-', '\\' };

    List<JSONObject> users = mongoTemplate.find(new Query(), JSONObject.class, Variables.col_usersExtraInfo);

    for (JSONObject user : users) {

      Object rolet = user.get("role");

      String role = (rolet != null) ? user.get("role").toString().toString() : "USER";

      if (role != null)
        user.put("roleClass", RoleClass.getRoleClass(role));
      else
        user.put("roleClass", RoleClass.getRoleClass("USER"));

      mongoTemplate.save(user, Variables.col_usersExtraInfo);

      int i = (++c * 100 / users.size());

      System.out.print("Processing role: " + i + "% " + animationChars[i % 4] + "\r");
    }

    mongoTemplate.save(new OneTime(Variables.ROLE_CORRECTION, true,
    new Date().getTime()), Variables.col_OneTime);

    System.out.println("Processing role: Done!");

  }

}

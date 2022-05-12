package parsso.idman.helpers.onetime;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.OneTime;
import parsso.idman.models.users.Role;
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

    List<JSONObject> users = mongoTemplate.find(new Query(), JSONObject.class,Variables.col_usersExtraInfo);

    for (JSONObject user : users) {
     
      Object rolet = user.get("role");
      
      String role = (rolet!=null) ? user.get("role").toString().toString(): "USER";
      user.remove("role");
      if (role!=null)
        user.put("role", getRoleClass(role));
      else
        user.put("role", getRoleClass("USER"));
      
      mongoTemplate.save(user,Variables.col_usersExtraInfo);

      int i = (++c * 100 / users.size());

      System.out.print("Processing role: " + i + "% " + animationChars[i % 4] + "\r");
    }

    OneTime oneTime1 = new OneTime(Variables.ROLE_CORRECTION, true,
        new Date().getTime());
    mongoTemplate.save(oneTime1, Variables.col_OneTime);

    System.out.println("Processing role: Done!");

  }

  private Role getRoleClass(String role) {
    int _id=0;
    switch(role){
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
    return new Role(_id,role);
  }
}

  

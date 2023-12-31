package parsso.idman.impls.role.subclass;

import java.util.List;
import java.util.Objects;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;

import net.minidev.json.JSONObject;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.UsersExtraInfo;

@SuppressWarnings("unchecked")
public class Update {
  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;

  public Update(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public HttpStatus update(String doerID, String role, JSONObject users) {
    int i = 0;
    for (String userId : (List<String>) users.get("names")) {
      try {
        UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(new Query(Criteria.where("_id").is(userId)),
            UsersExtraInfo.class, Variables.col_usersExtraInfo);
        String oldRole = Objects.requireNonNull(usersExtraInfo).getRole();
        usersExtraInfo.setRole(role);
        mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);
        uniformLogger.info(doerID,
            new ReportMessage(Variables.MODEL_ROLE, userId, "", "change", Variables.RESULT_SUCCESS,
                "from \"" + oldRole + "\" to \"" + role + "\""));

      } catch (Exception e) {
        i++;
        e.printStackTrace();
        uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_ROLE, userId, "", "change",
            Variables.RESULT_FAILED, "due to writing to ldap"));

      }

      if (i > 0) {
        uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_ROLE, userId, "", "change",
            Variables.RESULT_SUCCESS, "partially done"));
        return HttpStatus.PARTIAL_CONTENT;

      }
    }
    return HttpStatus.OK;

  }
}

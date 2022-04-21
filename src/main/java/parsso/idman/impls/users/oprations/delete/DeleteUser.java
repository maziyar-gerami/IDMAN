package parsso.idman.impls.users.oprations.delete;

import lombok.val;
import net.minidev.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.BuildDnUser;
import parsso.idman.impls.users.oprations.update.helper.Parameters;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.UserRepo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@SuppressWarnings({ "unchecked" })
public class DeleteUser extends Parameters implements UserRepo.UsersOp.Delete {
  public DeleteUser(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UniformLogger uniformLogger,
      UserRepo.UsersOp.Retrieve userOpRetrieve) {
    super(ldapTemplate, mongoTemplate, uniformLogger, userOpRetrieve);
  }

  @Override
  public List<String> remove(String doer, JSONObject jsonObject) {

    List<User> people = new LinkedList<>();
    List<String> unDeletable = new LinkedList<>();
    if (jsonObject.size() == 0)
      people = userOpRetrieve.fullAttributes();
    else {
      val jsonArray = (ArrayList<String>) jsonObject.get("names");
      for (String s : jsonArray) {
        User user = userOpRetrieve.retrieveUsers(s);
        if (userOpRetrieve.retrieveUsers(s) != null)
          people.add(user);
      }
    }

    if (people != null)
      for (User user : people) {
        if (user.isUnDeletable()) {
          unDeletable.add(user.get_id().toString());
          continue;
        }
        Query query = new Query(new Criteria("_id").is(user.get_id().toString()));

        try {
          ldapTemplate.unbind(new BuildDnUser(BASE_DN).buildDn(user.get_id().toString()));
          mongoTemplate.remove(query, UsersExtraInfo.class, Variables.col_usersExtraInfo);
          uniformLogger.info(doer, new ReportMessage(Variables.MODEL_USER, user.get_id().toString(), "",
              Variables.ACTION_DELETE, Variables.RESULT_SUCCESS, ""));

        } catch (Exception e) {
          e.printStackTrace();
          uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_USER, user.get_id().toString(), "",
              Variables.ACTION_DELETE, Variables.RESULT_FAILED, "unknown reason"));

        }

      }

    return unDeletable;
  }
}

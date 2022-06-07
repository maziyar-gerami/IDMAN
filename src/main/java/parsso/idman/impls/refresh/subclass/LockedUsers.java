package parsso.idman.impls.refresh.subclass;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.PresentFilter;
import org.springframework.stereotype.Service;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;

@Service
public class LockedUsers {

  MongoTemplate mongoTemplate;
  LdapTemplate ldapTemplate;
  UniformLogger uniformLogger;

  @Autowired
  public LockedUsers(MongoTemplate mongoTemplate, LdapTemplate ldapTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.ldapTemplate = ldapTemplate;
    this.uniformLogger = uniformLogger;
  }

  public void refresh() {

    SearchControls searchControls = new SearchControls();
    searchControls.setReturningAttributes(new String[] { "*", "+" });
    searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

    AndFilter andFilter = new AndFilter();

    andFilter.and(new PresentFilter("pwdAccountLockedTime"));
    andFilter.and(new NotFilter(new EqualsFilter("pwdAccountLockedTime", "00010101000000Z")));

    List<User> users = new LinkedList<>();
    try {
      users = ldapTemplate.search(Prefs.get(Variables.PREFS_BASE_DN), andFilter.encode(),
          new UserAttributeMapper(mongoTemplate));
    } catch (Exception ignored) {
    }
    for (User user : users) {
      Query query = new Query(Criteria.where("_id").is(user.get_id()));
      UsersExtraInfo simpleUser = mongoTemplate.findOne(query, UsersExtraInfo.class,
          Variables.col_usersExtraInfo);
      try {
        if (!Objects.requireNonNull(simpleUser).getStatus().equalsIgnoreCase("lock")) {
          simpleUser.setStatus("lock");
          uniformLogger.info("System",
              new ReportMessage(Variables.MODEL_USER, user.get_id(), "", Variables.ACTION_LOCK, "", ""));
          mongoTemplate.save(simpleUser, Variables.col_usersExtraInfo);
        }
      } catch (Exception ignored) {
      }

    }

    List<UsersExtraInfo> simpleUsers = mongoTemplate.find(new Query(Criteria.where("status").is("lock")),
        UsersExtraInfo.class, Variables.col_usersExtraInfo);
    for (UsersExtraInfo simple : simpleUsers) {
      Query query = new Query(Criteria.where("_id").is(simple.get_id()));

      if (ldapTemplate
          .search("ou=People," + Prefs.get(Variables.PREFS_BASE_DN),
              new EqualsFilter("uid", simple.get_id().toString()).encode(),
              searchControls, new UserAttributeMapper(mongoTemplate))
          .size() == 0) {

        simple.setStatus("enable");

        mongoTemplate.remove(query, UsersExtraInfo.class, Variables.col_usersExtraInfo);
        mongoTemplate.save(simple, Variables.col_usersExtraInfo);

        uniformLogger.info(Variables.DOER_SYSTEM, new ReportMessage(Variables.MODEL_USER, simple.get_id(), "",
            Variables.ACTION_UNLOCK, Variables.RESULT_SUCCESS, "due to time pass"));

      }

    }

  }

}

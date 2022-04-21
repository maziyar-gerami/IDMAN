package parsso.idman.impls.users.oprations.update.helper;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.BuildAttributes;
import parsso.idman.helpers.user.BuildDnUser;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;

import javax.naming.directory.SearchControls;
import java.util.List;

public class UsersWithSpecificOU {
  final UniformLogger uniformLogger;
  final LdapTemplate ldapTemplate;
  final MongoTemplate mongoTemplate;
  final BuildAttributes buildAttributes;
  final String BASE_DN;

  public UsersWithSpecificOU(UniformLogger uniformLogger, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,
      BuildAttributes buildAttributes, UserAttributeMapper userAttributeMapper, String BASE_DN) {
    this.uniformLogger = uniformLogger;
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
    this.buildAttributes = buildAttributes;
    this.BASE_DN = BASE_DN;
  }

  public void updateUsersWithSpecificOU(String doerID, String old_ou, String new_ou) {

    try {

      for (User user : getUsersOfOu(old_ou)) {

        DirContextOperations context = buildAttributes.buildAttributes(doerID, user.get_id().toString(), user,
            new BuildDnUser(BASE_DN).buildDn(user.get_id().toString()));

        context.removeAttributeValue("ou", old_ou);
        context.addAttributeValue("ou", new_ou);

        try {
          ldapTemplate.modifyAttributes(context);
        } catch (Exception e) {
          e.printStackTrace();
          uniformLogger.warn(doerID,
              new ReportMessage(Variables.MODEL_USER, user.get_id().toString(), Variables.MODEL_GROUP,
                  Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "writing to ldap"));

        }
      }
      uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, doerID, Variables.MODEL_GROUP,
          Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, old_ou, new_ou, ""));

    } catch (Exception e) {
      e.printStackTrace();
      uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, doerID, Variables.MODEL_GROUP,
          Variables.ACTION_UPDATE, Variables.RESULT_FAILED, new_ou, "writing to ldap"));

    }
  }

  public List<User> getUsersOfOu(String ou) {
    SearchControls searchControls = new SearchControls();
    searchControls.setReturningAttributes(new String[] { "*", "+" });
    searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

    final AndFilter andFilter = new AndFilter();
    andFilter.and(new EqualsFilter("objectclass", "person"));
    andFilter.and(new EqualsFilter("ou", ou));

    List<User> users = ldapTemplate.search("ou=People," + BASE_DN, andFilter.toString(), searchControls,
        new UserAttributeMapper(mongoTemplate));

    for (User user : users)
      user.setUsersExtraInfo(mongoTemplate.findOne(new Query(Criteria.where("_id").is(user.get_id().toString())),
          UsersExtraInfo.class, Variables.col_usersExtraInfo));

    return users;
  }

}

package parsso.idman.helpers.onetime;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.models.other.OneTime;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

import java.util.Date;
import java.util.List;

import javax.naming.directory.SearchControls;

public class MongoUserDocumentFix {
  final MongoTemplate mongoTemplate;
  final UsersRetrieveRepo usersOpRetrieve;
  final LdapTemplate ldapTemplate;
  final String BASE_DN;

  MongoUserDocumentFix(MongoTemplate mongoTemplate, UsersRetrieveRepo usersOp, LdapTemplate ldapTemplate,
      UserAttributeMapper userAttributeMapper) {
    this.mongoTemplate = mongoTemplate;
    this.usersOpRetrieve = usersOp;
    this.ldapTemplate = ldapTemplate;
    this.BASE_DN = Prefs.get(Variables.PREFS_BASE_DN);
  }

  public void run() {
    long count = mongoTemplate.count(new Query(), Variables.col_usersExtraInfo);
    int number = 50;
    int it = (int) (Math.floor(count / number) + 1);
    char[] animationChars = new char[] { '|', '/', '-', '\\' };

    for (int i = 0; i < it; i++) {
      int skip = i * number;
      List<UsersExtraInfo> usersExtraInfos = mongoTemplate.find(new Query().skip(skip).limit(number),
          UsersExtraInfo.class, Variables.col_usersExtraInfo);

      for (UsersExtraInfo usersExtraInfo : usersExtraInfos) {

        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[] { "*", "+" });
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        List<User> people = ldapTemplate.search("ou=People," + BASE_DN,
            new EqualsFilter("uid", usersExtraInfo.getUserId()).encode(), searchControls,
            new UserAttributeMapper(mongoTemplate));

        Update update = new Update();
        update.set("mobile", people.get(0).getMobile());

        mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(usersExtraInfo.getUserId())), update,
            Variables.col_usersExtraInfo);
      }
      System.out.print("Adding mobile's user Mongo: " + i / it * 100 + "% " + animationChars[i % 4] + "\r");
    }

    System.out.print("Adding mobile's user Mongo: Done!");

    int c = 0;
    List<UsersExtraInfo> usersExtraInfos = mongoTemplate.find(new Query(), UsersExtraInfo.class,
        Variables.col_usersExtraInfo);
    for (UsersExtraInfo usersExtraInfo : usersExtraInfos) {
      String userId = usersExtraInfo.getUserId();

      usersExtraInfo.setUserId(null);

      usersExtraInfo.set_id(userId);
      try {
        mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);
        mongoTemplate.remove(new Query(Criteria.where("userId").is(userId)), Variables.col_usersExtraInfo);

      } catch (InvalidDataAccessApiUsageException in) {
        continue;
      }
      c = (int) ((c++) * 100 / count);
      System.out.print("Fixing user Mongo: " + c + "% " + animationChars[c % 4] + "\r");

    }

    OneTime oneTime1 = new OneTime(Variables.MOBILE_TO_MONGO, true, new Date().getTime());
    mongoTemplate.save(oneTime1, Variables.col_OneTime);

    System.out.println("Fixing user Mongo: Done!");

  }
}

package parsso.idman.impls.users.oprations.retrieve.sub;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.SimpleUserAttributeMapper;
import parsso.idman.impls.logs.transcripts.ServicesOfObject;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.LogsRepo;
import parsso.idman.repos.ServiceRepo;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

import javax.naming.directory.SearchControls;
import java.util.LinkedList;
import java.util.List;

public class Conditional {
  UsersRetrieveRepo userOpRetrieve;
  LdapTemplate ldapTemplate;
  ServiceRepo.Retrieve serviceRepo;
  MongoTemplate mongoTemplate;
  LogsRepo.TranscriptRepo transcriptRepo;

  public Conditional(UsersRetrieveRepo userOpRetrieve,
      LogsRepo.TranscriptRepo transcriptRepo, MongoTemplate mongoTemplate, ServiceRepo.Retrieve serviceRepo) {
    this.userOpRetrieve = userOpRetrieve;
    this.transcriptRepo = transcriptRepo;
    this.serviceRepo = serviceRepo;
    this.mongoTemplate = mongoTemplate;
  }

  public Conditional(UsersRetrieveRepo userOpRetrieve,
      MongoTemplate mongoTemplate, ServiceRepo.Retrieve serviceRepo) {
    this.userOpRetrieve = userOpRetrieve;
    this.serviceRepo = serviceRepo;
    this.mongoTemplate = mongoTemplate;
  }

  public Conditional() {
  }

  public Conditional(LdapTemplate ldapTemplate) {

    this.ldapTemplate = ldapTemplate;
  }

  public User licensed(String userId) {

    User user = userOpRetrieve.retrieveUsers(userId);

    if (user != null)
      user.setServices(new ServicesOfObject(serviceRepo, mongoTemplate).servicesOfUser(userId));

    return user;
  }

  public List<UsersExtraInfo> group(String groupId) {

    SearchControls searchControls = new SearchControls();
    searchControls.setReturningAttributes(new String[] { "*", "+" });
    searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

    return ldapTemplate.search("ou=People," + Prefs.get(Variables.PREFS_BASE_DN), new EqualsFilter("ou", groupId).encode(), searchControls,
        new SimpleUserAttributeMapper());

  }

  public User.ListUsers group(String groupId, int page, int number) {

    List<UsersExtraInfo> users = group(groupId);

    CollectionUtils.filter(users, PredicateUtils.notNullPredicate());

    int n = Math.min((page) * number, users.size());

    int size = users.size();
    int start = (page - 1) * number;

    List<UsersExtraInfo> relativeUsers = new LinkedList<>();

    for (int i = start; i < n; i++)
      relativeUsers.add(users.get(i));

    CollectionUtils.filter(relativeUsers, PredicateUtils.notNullPredicate());

    return new User.ListUsers(size, relativeUsers, (int) Math.ceil((double) size / (double) number));

  }

}

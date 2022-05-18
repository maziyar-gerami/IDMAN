package parsso.idman.impls.users.oprations.retrieve.sub;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;

import parsso.idman.helpers.user.SimpleUserAttributeMapper;
import parsso.idman.impls.logs.transcripts.ServicesOfObject;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.LogsRepo;
import parsso.idman.repos.ServiceRepo;
import parsso.idman.repos.UserRepo;

import javax.naming.directory.SearchControls;
import java.util.LinkedList;
import java.util.List;

public class Conditional {
  UserRepo.UsersOp.Retrieve userOpRetrieve;
  LdapTemplate ldapTemplate;
  ServiceRepo serviceRepo;
  MongoTemplate mongoTemplate;
  LogsRepo.TranscriptRepo transcriptRepo;
  String BASE_DN;

  public Conditional(UserRepo.UsersOp.Retrieve userOpRetrieve,
      LogsRepo.TranscriptRepo transcriptRepo, MongoTemplate mongoTemplate, ServiceRepo serviceRepo) {
    this.userOpRetrieve = userOpRetrieve;
    this.transcriptRepo = transcriptRepo;
    this.serviceRepo = serviceRepo;
    this.mongoTemplate = mongoTemplate;
  }

  public Conditional(UserRepo.UsersOp.Retrieve userOpRetrieve,
      MongoTemplate mongoTemplate, ServiceRepo serviceRepo) {
    this.userOpRetrieve = userOpRetrieve;
    this.serviceRepo = serviceRepo;
    this.mongoTemplate = mongoTemplate;
  }

  public Conditional() {
  }

  public Conditional(LdapTemplate ldapTemplate, String BASE_DN) {

    this.ldapTemplate = ldapTemplate;
    this.BASE_DN = BASE_DN;
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

    return ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("ou", groupId).encode(), searchControls,
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

package parsso.idman.impls.users.oprations.retrieve.sub;

import javax.naming.directory.SearchControls;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.impls.users.oprations.retrieve.helper.SkyroomAccess;
import parsso.idman.impls.users.oprations.retrieve.helper.UserRole;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FullAttributes {
  final LdapTemplate ldapTemplate;
  final MongoTemplate mongoTemplate;

  public FullAttributes(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate) {
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
  }

  public List<User> get() {
    SearchControls searchControls = new SearchControls();
    searchControls.setReturningAttributes(new String[] { "*", "+" });
    searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

    final AndFilter andFilter = new AndFilter();
    andFilter.and(new EqualsFilter("objectclass", "person"));

    List<User> people = ldapTemplate.search("ou=People," + Prefs.get(Variables.PREFS_BASE_DN), andFilter.toString(),
        searchControls,
        new UserAttributeMapper(mongoTemplate));
    List<User> relatedPeople = new LinkedList<>();

    for (User user : people) {
      if (user != null && user.getDisplayName() != null) {
        relatedPeople.add(user);
      }

    }

    Collections.sort(relatedPeople);

    return relatedPeople;
  }

  public User get(String userId) {
    userId = userId.toLowerCase();

    SearchControls searchControls = new SearchControls();
    searchControls.setReturningAttributes(new String[] { "*", "+" });
    searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

    User user = new User();
    UsersExtraInfo usersExtraInfo;
    List<User> people = ldapTemplate.search("ou=People," + Prefs.get(Variables.PREFS_BASE_DN),
        new EqualsFilter("uid", userId).encode(),
        searchControls, new UserAttributeMapper(mongoTemplate));

    if (people.size() != 0) {
      user = people.get(0);
      Query query = new Query(Criteria.where("_id").is(user.get_id().toString()));
      usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, Variables.col_usersExtraInfo);
      user.setUsersExtraInfo(mongoTemplate.findOne(query, UsersExtraInfo.class, Variables.col_usersExtraInfo));
      try {
        user.setUnDeletable(Objects.requireNonNull(usersExtraInfo).isUnDeletable());
      } catch (Exception e) {
        user.setUnDeletable(false);
      }

    } else
      return null;

    user.setSkyroomAccess(new SkyroomAccess(mongoTemplate).get(user));

    if (user.getRole() == null)
      user = UserRole.set(user);

    if (user.getRole().equals("USER") && !Boolean
        .parseBoolean(new Settings(mongoTemplate).retrieve(Variables.USER_PROFILE_ACCESS).getValue()))
      user.setProfileInaccessibility(true);

    if (user.get_id() == null)
      return null;

    else
      return user;
  }

}

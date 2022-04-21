package parsso.idman.configs;

import java.util.ArrayList;
import java.util.Collection;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.UsersExtraInfo;

@SuppressWarnings("rawtypes")
@Service
public class CasUserDetailService implements AuthenticationUserDetailsService {
  final MongoTemplate mongoTemplate;

  public CasUserDetailService(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
    CasAssertionAuthenticationToken casAssertionAuthenticationToken 
        = (CasAssertionAuthenticationToken) token;
    AttributePrincipal principal = casAssertionAuthenticationToken.getAssertion().getPrincipal();
    Collection<SimpleGrantedAuthority> collection = new ArrayList<>();
    Query query = new Query(Criteria.where("_id").is(principal.getName().toLowerCase()));

    UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query,
        UsersExtraInfo.class, Variables.col_usersExtraInfo);

    if (usersExtraInfo == null) {
      mongoTemplate.save(new UsersExtraInfo(principal.getName()), Variables.col_usersExtraInfo);
    }

    if (usersExtraInfo.getRole() == null) {
      collection.add(new SimpleGrantedAuthority("ROLE_" + "USER"));
    } else if (usersExtraInfo.getRole().equals("SUPERUSER")
        || usersExtraInfo.get_id().toString().equalsIgnoreCase("su")) {
      collection.add(new SimpleGrantedAuthority("ROLE_" + "SUPERUSER"));
    } else if (usersExtraInfo.getRole() != null) {
      collection.add(new SimpleGrantedAuthority("ROLE_" + usersExtraInfo.getRole()));
    }

    return new User(principal.getName(), "", collection);
  }
}

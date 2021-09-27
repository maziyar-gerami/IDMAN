package parsso.idman.configs;


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
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Users.UsersExtraInfo;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("rawtypes")
@Service
public class CasUserDetailService implements AuthenticationUserDetailsService {
	final
	MongoTemplate mongoTemplate;

	public CasUserDetailService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		CasAssertionAuthenticationToken casAssertionAuthenticationToken = (CasAssertionAuthenticationToken) token;
		AttributePrincipal principal = casAssertionAuthenticationToken.getAssertion().getPrincipal();
		Collection<SimpleGrantedAuthority> collection = new ArrayList<>();
		Query query = new Query(Criteria.where("userId").is(principal.getName().toLowerCase()));
		String collection1 = Variables.col_usersExtraInfo;

		UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, collection1);

		if (usersExtraInfo == null) {
			usersExtraInfo = new UsersExtraInfo(principal.getName());
			mongoTemplate.save(usersExtraInfo, collection1);
		}

		if (usersExtraInfo.getRole() == null)
			collection.add(new SimpleGrantedAuthority("ROLE_" + "USER"));

		else if (usersExtraInfo.getRole().equals("SUPERADMIN") || usersExtraInfo.getUserId().equalsIgnoreCase("su"))
			collection.add(new SimpleGrantedAuthority("ROLE_" + "SUPERADMIN"));

		else if (usersExtraInfo.getRole() != null)
			collection.add(new SimpleGrantedAuthority("ROLE_" + usersExtraInfo.getRole()));

		return new User(principal.getName(), "", collection);

	}

}

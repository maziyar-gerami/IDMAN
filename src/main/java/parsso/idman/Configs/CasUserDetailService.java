package parsso.idman.Configs;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import parsso.idman.RepoImpls.UserRepoImpl;
import parsso.idman.Repos.EventRepo;
import parsso.idman.Repos.UserRepo;

import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class CasUserDetailService implements AuthenticationUserDetailsService {

    @Value("${administrator.ou.id}")
    private String adminId = "1598656906150";



    @Override
    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
        CasAssertionAuthenticationToken casAssertionAuthenticationToken = (CasAssertionAuthenticationToken) token;
        AttributePrincipal principal = casAssertionAuthenticationToken.getAssertion().getPrincipal();
        Map attributes = principal.getAttributes();
        String role = null;
        try {
            List<String> lst = (List) attributes.get("ou");
            for (String id:lst) {
                if (id.equals(adminId)) {
                    role = "ADMIN";
                    break;
                }
            }
        } catch (Exception e) {
            String id = (String) attributes.get("ou");
            if (id.equals(adminId))
                role = "ADMIN";

        }

        if(role == null)
            role = "USER";


        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        Collection<SimpleGrantedAuthority> collection = new ArrayList<SimpleGrantedAuthority>();
        collection.add(new SimpleGrantedAuthority("ROLE_"+role));
        return new User(principal.getName(), "", collection);
    }

}

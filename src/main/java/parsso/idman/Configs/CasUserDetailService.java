package parsso.idman.Configs;


import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CasUserDetailService implements AuthenticationUserDetailsService {

    String adminId;
    public CasUserDetailService(String Id){

        adminId = Id;

    }



    @Override
    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
        CasAssertionAuthenticationToken casAssertionAuthenticationToken = (CasAssertionAuthenticationToken) token;
        AttributePrincipal principal = casAssertionAuthenticationToken.getAssertion().getPrincipal();
        Collection<SimpleGrantedAuthority> collection = new ArrayList<SimpleGrantedAuthority>();
        Map attributes = principal.getAttributes();
        String role = null;
        if (principal.getName().equals("su")) {
            collection.add(new SimpleGrantedAuthority("ROLE_" + "SUPERADMIN"));
        }

        try {
            List<String> lst = (List) attributes.get("ou");
            for (String id : lst) {

                if (id.equals(adminId)) {
                    collection.add(new SimpleGrantedAuthority("ROLE_" + "ADMIN"));
                    break;
                }
            }
        } catch (Exception e) {
            String id = (String) attributes.get("ou");

            if (id != null && id.equals(adminId))
                collection.add(new SimpleGrantedAuthority("ROLE_" + "ADMIN"));

        }

        if (role == null)
            collection.add(new SimpleGrantedAuthority("ROLE_" + "USER"));

        return new User(principal.getName(), "", collection);
    }

}

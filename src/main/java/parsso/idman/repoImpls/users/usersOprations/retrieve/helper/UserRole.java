package parsso.idman.repoImpls.users.usersOprations.retrieve.helper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import parsso.idman.models.users.User;

import java.util.ArrayList;
import java.util.List;

public class UserRole {

    public static User set(User p) {
        String role;

        try {
            role = p.getUsersExtraInfo().getRole();
            if(role == null)
                role = "USER";
        } catch (Exception e) {
            role = "USER";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities;

        if (auth != null) {
            updatedAuthorities = new ArrayList<>(auth.getAuthorities());

            if (auth.getName().equals(p.get_id().toString())) {
                updatedAuthorities.remove(0);

                updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));

                Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);

                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
        }
        p.setRole(role);

        return p;

    }

}

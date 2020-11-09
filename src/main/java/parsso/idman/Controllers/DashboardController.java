package parsso.idman.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
public class DashboardController {

    @Qualifier("userRepoImpl")
    @Autowired
    private UserRepo userRepo;

    @Value("${cas.url.logout.path}")
    private String casLogout;

    @Value("${administrator.ou.id}")
    private String adminOu;

    @GetMapping("/")
    public String Root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String Dashboard() {
        return "dashboard";
    }

    @GetMapping("/resetPassword")
    public String resetPassword() {
        return "resetPassword";
    }

    @GetMapping("/groups")
    public String Groups(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userRepo.retrieveUser(principal.getName());
            List<String> memberOf = user.getMemberOf();


            for (String group : memberOf) {
                if (group.equals(adminOu))
                    return "groups";
            }

        } catch (Exception e) {
            return "403";
        }
        return "403";
    }

    @GetMapping("/users")
    public String Users(HttpServletRequest request) {

        try {
            Principal principal = request.getUserPrincipal();
            User user = userRepo.retrieveUser(principal.getName());
            List<String> memberOf = user.getMemberOf();


            for (String group : memberOf) {
                if (group.equals(adminOu))
                    return "users";
            }

        } catch (Exception e) {
            return "403";
        }
        return "403";
    }

    @GetMapping("/events")
    public String Events(HttpServletRequest request) {
        Collection<? extends GrantedAuthority> s = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return "events";
    }

    @GetMapping("/configs")
    public String Configs(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userRepo.retrieveUser(principal.getName());
            List<String> memberOf = user.getMemberOf();


            for (String group : memberOf) {
                if (group.equals(adminOu))
                    return "configs";
            }

        } catch (Exception e) {
            return "403";
        }
        return "403";
    }

    @GetMapping("/settings")
    public String Settings() {
        return "settings";
    }

    @GetMapping("/errorpage")
    public String Error() {
        return "error";
    }

    @GetMapping("/privacy")
    public String Privacy() {
        return "privacy";
    }

    @GetMapping("/login")
    public String Login() {
        return "redirect:/login/cas";
    }

    @GetMapping("/resetPass")
    public String resetPass() {
        return "resetPass";
    }

    @GetMapping("/logout")
    public String logout(
            HttpServletRequest request,
            HttpServletResponse response,
            SecurityContextLogoutHandler logoutHandler) {
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        logoutHandler.logout(request, response, auth);
        new CookieClearingLogoutHandler(
                AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY)
                .logout(request, response, auth);
        return "redirect:" + casLogout;
    }


}
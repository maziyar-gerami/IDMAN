package parsso.idman.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
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
    public String Dashboard(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUsers(principal.getName());

        return "dashboard";
    }

    @GetMapping("/resetPassword")
    public String resetPassword() {
        return "resetPassword";
    }

    @GetMapping("/groups")
    public String Groups(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUsers(principal.getName());
        if (user.getUserId().equals("su"))
            return "groups";

        try {

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

    @GetMapping("/services")
    public String Services(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUsers(principal.getName());
        if (user.getUserId().equals("su"))
            return "services";

        try {
            if (user.getMemberOf().contains(adminOu))
                return "services";

        } catch (Exception e) {
            return "403";
        }
        return "403";
    }

    @GetMapping("/users")
    public String Users(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUsers(principal.getName());
        if (user.getUserId().equals("su"))
            return "users";

        try {

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

    @GetMapping("/audits")
    public String Audits(HttpServletRequest request) {
        return "audits";
    }

    @GetMapping("/events")
    public String Events(HttpServletRequest request) {
        return "events";
    }

    @GetMapping("/configs")
    public String Configs(HttpServletRequest request) {
        try {
            if (request.getUserPrincipal().getName().equals("su"))
                return "configs";


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
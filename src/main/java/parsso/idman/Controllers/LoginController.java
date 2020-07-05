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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import parsso.idman.Models.Person;
import parsso.idman.RepoImpls.PersonRepoImpl;
import parsso.idman.Repos.PersonRepo;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Value("${cas.url.logout.path}")
    private String casLogout;

    @Value("${cas.url.login.path}")
    private String casLogin;

    @Qualifier("personRepoImpl")
    @Autowired
    private PersonRepo personRepo;

    @GetMapping("/login")
    public String Login() {
        return "login";
    }

    @GetMapping("/resetPass")
    public String resetPass() {
        return "resetPass";
    }

    @GetMapping ("/api/user/password")
    public String changePassword(HttpServletRequest request,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam ("newPassword") String newPassword)
    {
        try {
            Principal principal = request.getUserPrincipal();
            return personRepo.changePassword(principal.getName(),currentPassword,newPassword);
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/logout")
    public String logout(
            HttpServletRequest request,
            HttpServletResponse response,
            SecurityContextLogoutHandler logoutHandler) {
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        logoutHandler.logout(request, response, auth );
        new CookieClearingLogoutHandler(
                AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY)
                .logout(request, response, auth);
        return "redirect:"+casLogout;
    }



}


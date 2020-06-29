package parsso.idman.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import parsso.idman.Models.Person;
import parsso.idman.RepoImpls.PersonRepoImpl;
import parsso.idman.Repos.PersonRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

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


    @GetMapping ("/api/users/validateToken/{uId}/{token}")
    public String resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token)
    {
        Person person = personRepo.checkToken(uId,token);

        if (person != null)
            return "resetPassword";
        else
            return "resetPassWrongTAI";
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
        return "auth/logout";
    }



}


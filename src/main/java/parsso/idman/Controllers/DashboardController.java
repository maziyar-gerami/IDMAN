package parsso.idman.Controllers;


import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import parsso.idman.Models.DashboardData.Dashboard;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class DashboardController {

    @Qualifier("userRepoImpl")
    @Autowired
    private UserRepo userRepo;

    @Value("${cas.url.logout.path}")
    private String casLogout;

    //*************************************** Pages ***************************************

    @GetMapping("/")
    public String Root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String Dashboard() {
        return "dashboard";
    }

    @GetMapping("/privacy")
    public String Privacy() {
        return "privacy";
    }

    @GetMapping("/errorpage")
    public String Error() {
        return "error";
    }

    @GetMapping("/login")
    public String Login() {
        return "redirect:/login/cas";
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


    //*************************************** APIs ***************************************

    /**
     * get the information for dashboard
     *
     * @return a json file containing tha data
     */
    @GetMapping("/api/dashboard")
    public ResponseEntity<Dashboard> retrieveDashboardData() throws ParseException, java.text.ParseException, IOException, InterruptedException {

        return new ResponseEntity<>(userRepo.retrieveDashboardData(), HttpStatus.OK);
    }


    //*************************************** Pages ***************************************

    @GetMapping("/users")
    public String Users() {
        return "users";
    }

    @GetMapping("/profile")
    public String Profile() {
        return "profile";
    }

    @GetMapping("/resetpassword")
    public String resetPass() {
        return "resetpassword";
    }

    @GetMapping("/newpassword")
    public String resetPassword() {
        return "newpassword";
    }

}
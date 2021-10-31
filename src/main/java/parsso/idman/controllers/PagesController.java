package parsso.idman.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import parsso.idman.helpers.user.DashboardData;
import parsso.idman.models.dashboardData.Dashboard;

@SuppressWarnings("SameReturnValue")
@Controller
public class PagesController implements ErrorController {


    private final DashboardData dashboardData;
    @Value("${cas.url.logout.path}")
    private String casLogout;

    @Autowired
    public PagesController(DashboardData dashboardData) {
        this.dashboardData = dashboardData;
    }

    //************************************* Pages ****************************************

    @GetMapping("/publicmessages")
    public String PublicMessages() {
        return "publicmessages";
    }

    @GetMapping("/groups")
    public String getPageGroups() {
        return "groups";
    }

    @GetMapping("/")
    public String Root() {
        return "redirect:/dashboard";
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/roles")
    public String getPageRoles() {
        return "roles";
    }

    @GetMapping("/reports")
    public String getPageReports() {
        return "reports";
    }

    @GetMapping("/events")
    public String getPageEvents() {
        return "events";
    }

    @GetMapping("/audits")
    public String getPageAudits() {
        return "audits";
    }

    @GetMapping("/transcripts")
    public String getPageTranscripts() {
        return "transcripts";
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/configs")
    public String getPageConfigs() {
        return "configs";
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/ticketing")
    public String Reports() {
        return "ticketing";
    }

    @SuppressWarnings("SameReturnValue")
    @RequestMapping("/error")
    public String handleError() {
        return "redirect:/errorpage";
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/services")
    public String getPageServices() {
        return "services";
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/createservice")
    public String CreateService() {
        return "createservice";
    }


    @SuppressWarnings("SameReturnValue")
    @GetMapping("/403")
    public String AccessDenied() {
        return "403";
    }

    @Override
    public String getErrorPath() {
        return null;
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
    public String logout(){
        return "redirect:" + casLogout;
    }

    //************************************* APIs ****************************************

    @GetMapping("/api/dashboard")
    public ResponseEntity<Dashboard> retrieveDashboardData() {

        return new ResponseEntity<>(dashboardData.retrieveDashboardData(), HttpStatus.OK);
    }

    //************************************* Pages ****************************************

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
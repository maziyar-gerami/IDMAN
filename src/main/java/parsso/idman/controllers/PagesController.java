package parsso.idman.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import parsso.idman.Helpers.User.DashboardData;
import parsso.idman.Models.DashboardData.Dashboard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("SameReturnValue")
@Controller
public class PagesController implements ErrorController {


	private DashboardData dashboardData;

	@Autowired
	public PagesController(DashboardData dashboardData){
		this.dashboardData = dashboardData;
	}
	@Value("${cas.url.logout.path}")
	private String casLogout;

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
		return "reportsController";
	}

	@SuppressWarnings("SameReturnValue")
	@RequestMapping("/error")
	public String handleError() {
		return "redirect:/errorpage";
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
		//noinspection SpringMVCViewInspection
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
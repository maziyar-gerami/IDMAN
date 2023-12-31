package parsso.idman.controllers;

import io.jsonwebtoken.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.DashboardData;
import parsso.idman.models.response.Response;

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

  @GetMapping("/notifications")
  public String publicMessages() {
    return "index";
  }

  @GetMapping("/groups")
  public String getPageGroups() {
    return "index";
  }

  @GetMapping("/")
  public String root() {
    return "index";
  }

  @GetMapping("/roles")
  public String getPageRoles() {
    return "index";
  }

  @GetMapping("/reports")
  public String getPageReports() {
    return "index";
  }

  @GetMapping("/events")
  public String getPageEvents() {
    return "index";
  }

  @GetMapping("/audits")
  public String getPageAudits() {
    return "index";
  }

  @GetMapping("/accessreports")
  public String getPageTranscripts() {
    return "index";
  }

  @GetMapping("/settings")
  public String getPageSettings() {
    return "index";
  }

  @GetMapping("/ticketing")
  public String reports() {
    return "index";
  }

  @RequestMapping("/error")
  public String handleError() {
    return "redirect:/404";
  }

  @GetMapping("/services")
  public String getPageServices() {
    return "index";
  }

  @GetMapping("/403")
  public String accessDenied() {
    return "index";
  }

  @GetMapping("/privacy")
  public String privacy() {
    return "index";
  }

  @GetMapping("/errorpage")
  public String error() {
    return "redirect:/404";
  }

  @GetMapping("/login")
  public String login() {
    return "redirect:/login/cas";
  }

  @GetMapping("/changepassword")
  public String changepassword() {
    return "index";
  }

  @GetMapping("/users")
  public String users() {
    return "index";
  }

  @GetMapping("/profile")
  public String profile() {
    return "index";
  }

  @GetMapping("/resetpassword")
  public String resetPass() {
    return "index";
  }

  @GetMapping("/404")
  public String resetPassword() {
    return "index";
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

  
  /** 
   * this method returns an overview of entire system.
   * @param lang which demonstrate language
   * @return a Response containing overview data
   * @throws IOException which throws I/O exceptions for counting services
   * @throws NoSuchFieldException which shows that compalsury parameters not exist.
   * @throws IllegalAccessException which shows access level violation
   */
  // ************************************* APIs
  // ****************************************

  @GetMapping("/api/dashboard")
  public ResponseEntity<Response> retrieveDashboardData(
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws IOException, NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(
      dashboardData.retrieveDashboardData(), Variables.MODEL_DASHBOARD,
        HttpStatus.OK.value(), lang), HttpStatus.OK);
  }

  @Override
  public String getErrorPath() {
    return null;
  }

}
package parsso.idman.controllers;

import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

  // ************************************* Pages
  // ****************************************

  @GetMapping("/")
  public String root() {
    return "index";
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
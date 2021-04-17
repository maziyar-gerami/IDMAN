package parsso.idman.Controllers;


import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Repos.SystemRefresh;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class SystemRefreshController {
    @Autowired
    SystemRefresh systemRefresh;

    @GetMapping("/api/refresh/all")
    public HttpEntity<HttpStatus> all(HttpServletRequest request) throws IOException, ParseException {
        return new ResponseEntity<>(systemRefresh.all(request.getUserPrincipal().getName()));
    }

    @GetMapping("/api/refresh/services")
    public ResponseEntity<HttpStatus> services(HttpServletRequest request) throws IOException, ParseException {
        return new ResponseEntity<>(systemRefresh.serivceRefresh(request.getUserPrincipal().getName()));
    }

    @GetMapping("/api/refresh/captchas")
    public HttpEntity<HttpStatus> captchas(HttpServletRequest request){
        return new ResponseEntity<>(systemRefresh.captchaRefresh(request.getUserPrincipal().getName()));
    }

    @GetMapping("/api/refresh/users")
    public HttpEntity<HttpStatus> users(HttpServletRequest request) throws IOException {
        return new ResponseEntity<>(systemRefresh.userRefresh("maziyar"));
    }

    @GetMapping("/api/refresh/lockedUsers")
    public HttpEntity<HttpStatus> lockedUsers() throws IOException {
        return new ResponseEntity<>(systemRefresh.refreshLockedUsers());
    }
}

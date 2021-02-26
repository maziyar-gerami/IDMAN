package parsso.idman.Controllers;


import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Repos.SystemRefresh;

import java.io.IOException;

@RestController
public class SystemRefreshController {
    @Autowired
    SystemRefresh systemRefresh;

    @GetMapping("/api/refresh/all")
    public HttpEntity<HttpStatus> all() throws IOException, ParseException {
        return new ResponseEntity<>(systemRefresh.all());
    }

    @GetMapping("/api/refresh/services")
    public ResponseEntity<HttpStatus> services() throws IOException, ParseException {
        return new ResponseEntity<>(systemRefresh.serivceRefresh());
    }

    @GetMapping("/api/refresh/captchas")
    public HttpEntity<HttpStatus> captchas(){
        return new ResponseEntity<>(systemRefresh.captchaRefresh());
    }

    @GetMapping("/api/refresh/users")
    public HttpEntity<HttpStatus> users() throws IOException {
        return new ResponseEntity<>(systemRefresh.userRefresh());
    }

}

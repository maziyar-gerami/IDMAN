package parsso.idman.controllers;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.repos.SystemRefresh;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/refresh")
public class RefreshController {
    final SystemRefresh refresh;

    public RefreshController(SystemRefresh refresh) {
        this.refresh = refresh;
    }

    @GetMapping
    public HttpEntity<HttpStatus> all(HttpServletRequest request, @RequestParam(name = "type", defaultValue = "") String type) {
        switch (type) {
            case "service":
                return new ResponseEntity<>(refresh.serviceRefresh(request.getUserPrincipal().getName()));

            case "captcha":
                return new ResponseEntity<>(refresh.captchaRefresh(request.getUserPrincipal().getName()));

            case "user":
                return new ResponseEntity<>(refresh.userRefresh(request.getUserPrincipal().getName()));

            default:
                return new ResponseEntity<>(refresh.all(request.getUserPrincipal().getName()));

        }

    }

}

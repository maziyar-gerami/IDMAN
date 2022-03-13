package parsso.idman.controllers;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import parsso.idman.helpers.Variables;
import parsso.idman.models.response.Response;
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
    public ResponseEntity<Response> all(HttpServletRequest request, @RequestParam(name = "type", defaultValue = "") String type,@RequestParam(name = "lang",defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        switch (type) {
            case "services":
                return new ResponseEntity<>(new Response(null, Variables.MODEL_REFRESH, refresh.serviceRefresh(request.getUserPrincipal().getName()).value(),lang),HttpStatus.OK);

            case "captcha":
                return new ResponseEntity<>(new Response(null, Variables.MODEL_REFRESH, refresh.captchaRefresh(request.getUserPrincipal().getName()).value(),lang), HttpStatus.OK) ;

            case "users":
                return new ResponseEntity<>(new Response(null, Variables.MODEL_REFRESH, refresh.userRefresh(request.getUserPrincipal().getName()).value(),lang), HttpStatus.OK);

            default:
                return new ResponseEntity<>(new Response(null, Variables.MODEL_REFRESH, refresh.all(request.getUserPrincipal().getName()).value(),lang), HttpStatus.OK);

        }

    }

}

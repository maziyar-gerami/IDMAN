package parsso.idman.Controllers;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "redirect:/errorpage";
    }

    @GetMapping("/403")
    public String AccessDenied() {
        return "403";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
package parsso.idman.controllers;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController implements ErrorController {
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
}
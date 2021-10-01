package parsso.idman.controllers;


import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Repos.systemRefresh.SystemRefresh;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class RefreshsController {
	final
	SystemRefresh refreshs;

	public RefreshsController(SystemRefresh refreshs) {
		this.refreshs = refreshs;
	}

	@GetMapping("/api/refresh/all")
	public HttpEntity<HttpStatus> all(HttpServletRequest request) throws IOException, ParseException {
		return new ResponseEntity<>(refreshs.all(request.getUserPrincipal().getName()));
	}

	@GetMapping("/api/refresh/services")
	public ResponseEntity<HttpStatus> services(HttpServletRequest request) throws IOException, ParseException {
		return new ResponseEntity<>(refreshs.serivceRefresh(request.getUserPrincipal().getName()));
	}

	@GetMapping("/api/refresh/captchas")
	public HttpEntity<HttpStatus> captchas(HttpServletRequest request) throws IOException, ParseException {
		return new ResponseEntity<>(refreshs.captchaRefresh(request.getUserPrincipal().getName()));
	}

	@GetMapping("/api/refresh/users")
	public HttpEntity<HttpStatus> users(HttpServletRequest request) throws IOException, ParseException {
		return new ResponseEntity<>(refreshs.userRefresh(request.getUserPrincipal().getName()));
	}

	@GetMapping("/api/refresh/lockedUsers")
	public HttpEntity<HttpStatus> lockedUsers() throws IOException, ParseException {
		return new ResponseEntity<>(refreshs.refreshLockedUsers());
	}
}

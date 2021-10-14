package parsso.idman.controllers.ok.users;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/api/user")
public class User {
	private final UserRepo userRepo;

	@Autowired
	public User(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@GetMapping("/api/user")
	public ResponseEntity<parsso.idman.Models.Users.User> retrieveUser(HttpServletRequest request) throws IOException, ParseException {

		return new ResponseEntity<>(userRepo.retrieveUsers(request.getUserPrincipal().getName()), HttpStatus.OK);
	}

	@PutMapping("/api/user")
	public ResponseEntity<HttpStatus> updateUser(HttpServletRequest request, @RequestBody parsso.idman.Models.Users.User user) throws IOException, ParseException {
		String userId = request.getUserPrincipal().getName();
		parsso.idman.Models.Users.User userResult = userRepo.update(userId, userId, user);
		HttpStatus code = (userResult == null ? HttpStatus.FORBIDDEN : HttpStatus.OK);

		return new ResponseEntity<>(code);
	}

	@GetMapping("/photo")
	public ResponseEntity<String> getImage(HttpServletResponse response, HttpServletRequest request) throws IOException, ParseException {
		parsso.idman.Models.Users.User user = userRepo.retrieveUsers(request.getUserPrincipal().getName());
		return new ResponseEntity<>(userRepo.showProfilePic(response, user), HttpStatus.OK);
	}

	@PostMapping("/photo")
	public RedirectView uploadProfilePic(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, ParseException {
		userRepo.uploadProfilePic(file, request.getUserPrincipal().getName());
		return new RedirectView("/dashboard");
	}

	@PutMapping("/password")
	public ResponseEntity<HttpStatus> changePassword(HttpServletRequest request,
													 @RequestBody JSONObject jsonObject) throws IOException, ParseException {
		Principal principal = request.getUserPrincipal();
		String oldPassword = jsonObject.getAsString("currentPassword");
		String newPassword = jsonObject.getAsString("newPassword");
		String token = jsonObject.getAsString("token");
		if (jsonObject.getAsString("token") != null) token = jsonObject.getAsString("token");

		return new ResponseEntity<>(userRepo.changePassword(principal.getName(), oldPassword, newPassword, token));

	}

	@GetMapping("/password/request")
	public ResponseEntity<Integer> requestSMS(HttpServletRequest request) throws IOException, ParseException {
		Principal principal = request.getUserPrincipal();
		parsso.idman.Models.Users.User user = userRepo.retrieveUsers(principal.getName());
		int status = userRepo.requestToken(user);

		if (status > 0)
			return new ResponseEntity<>(status, HttpStatus.OK);
		else
			return new ResponseEntity<>(status, HttpStatus.FORBIDDEN);
	}
}
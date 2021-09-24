package parsso.idman.Controllers;


import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Models.SkyRoom;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.SkyroomRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class SkyroomController {
	final UserRepo userRepo;
	SkyroomRepo runSkyroom;
	@Value("${skyroom.enable}")
	String skyroomEnable;

	public SkyroomController(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Autowired
	public SkyroomController(UserRepo userRepo, SkyroomRepo runSkyroom) {
		this.userRepo = userRepo;
		this.runSkyroom = runSkyroom;
	}

	@Deprecated
	@GetMapping("/api/skyroom")
	ResponseEntity<SkyRoom> hello(HttpServletRequest request) throws IOException, ParseException {
		SkyRoom skyRoom;
		User user = userRepo.retrieveUsers(request.getUserPrincipal().getName());
		if (skyroomEnable.equalsIgnoreCase("true")) {

			try {
				skyRoom = runSkyroom.Run(user);
				user.setSkyRoom(skyRoom);
			} catch (IOException e) {
				e.printStackTrace();
				user.setSkyRoom(null);
			}

			try {
				return new ResponseEntity<>(runSkyroom.Run(userRepo.retrieveUsers(request.getUserPrincipal().getName())), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		} else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
}

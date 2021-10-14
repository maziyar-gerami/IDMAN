package parsso.idman.controllers.ok.users;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import parsso.idman.Helpers.Communicate.InstantMessage;
import parsso.idman.Helpers.Communicate.Token;
import parsso.idman.Models.Users.User;
import parsso.idman.RepoImpls.SystemRefreshRepoImpl;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Repos.email.EmailService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/public")
public class Public {
	EmailService emailService;
	InstantMessage instantMessage;
	UserRepo userRepo;
	Token tokenClass;
	SystemRefreshRepoImpl systemRefreshRepoImpl;

	@Autowired
	public Public( EmailService emailService,Token tokenClass,
				   InstantMessage instantMessage, UserRepo userRepo , SystemRefreshRepoImpl systemRefreshRepoImpl){
		this.emailService = emailService;
		this.tokenClass = tokenClass;
		this. systemRefreshRepoImpl = systemRefreshRepoImpl;
		this.instantMessage = instantMessage;
		this.userRepo = userRepo;
	}


	@GetMapping("/sendMail")
	public ResponseEntity<Integer> sendMail(@RequestParam("email") String email,
	                                        @RequestParam(value = "uid",defaultValue ="") String uid,
	                                        @RequestParam("cid") String cid,
	                                        @RequestParam("answer") String answer) throws IOException, ParseException {
		int time =uid.equals("")?userRepo.sendEmail(email, null, cid, answer): userRepo.sendEmail(email, uid, cid, answer);
		return getIntegerResponseEntity(time);
	}

	static ResponseEntity<Integer> getIntegerResponseEntity(int time) {
		if (time > 0) {
			return new ResponseEntity<>(time, HttpStatus.OK);
		} else if (time == -1)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		else if (time == -2)
			return new ResponseEntity<>(HttpStatus.FOUND);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/sendSMS")
	public ResponseEntity<JSONObject> sendMessage(@RequestParam("mobile") String mobile,
	                                              @RequestParam("cid") String cid,
	                                              @RequestParam(value = "uid", defaultValue = "") String uid,
	                                              @RequestParam("answer") String answer) throws IOException, ParseException {

		int time = uid.equals("")?instantMessage.sendMessage(mobile, cid, answer) : instantMessage.sendMessage(mobile, uid, cid, answer);

		if (time > 0) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("time", time);
			jsonObject.put("userId", userRepo.getByMobile(mobile));

			return new ResponseEntity<>(jsonObject, HttpStatus.OK);
		} else if (time == -1)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		else if (time == -2)
			return new ResponseEntity<>(HttpStatus.FOUND);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/sendTokenUser/{uid}/{cid}/{answer}")
	public ResponseEntity<JSONObject> sendMessageUser(
			@PathVariable("uid") String uid,
			@PathVariable("cid") String cid,
			@PathVariable("answer") String answer) throws IOException, ParseException {
		User user = userRepo.retrieveUsers(uid);
		if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		int time = instantMessage.sendMessage(user, cid, answer);

		if (time > 0) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("time", time);
			jsonObject.put("userId", uid);
			return new ResponseEntity<>(jsonObject, HttpStatus.OK);
		} else if (time == -1)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		else if (time == -2)
			return new ResponseEntity<>(HttpStatus.FOUND);
		else
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/resetPass/{uid}/{token}")
	public ResponseEntity<HttpStatus> rebindLdapUser(@RequestParam("newPassword") String newPassword, @PathVariable("token") String token,
	                                                 @PathVariable("uid") String uid) throws IOException, ParseException {
		return new ResponseEntity<>(userRepo.resetPassword(uid, newPassword, token));
	}

	@GetMapping("/checkMail/{email}")
	public HttpEntity<List<JSONObject>> checkMail(@PathVariable("email") String email) {
		return new ResponseEntity<>(emailService.checkMail(email), HttpStatus.OK);
	}

	@GetMapping("/checkMobile/{mobile}")
	public HttpEntity<List<JSONObject>> checkMobile(@PathVariable("mobile") String mobile) {
		return new ResponseEntity<>(instantMessage.checkMobile(mobile), HttpStatus.OK);
	}

	@GetMapping("/getName/{uid}/{token}")
	public ResponseEntity<User> getName(@PathVariable("uid") String uid, @PathVariable("token") String token) throws IOException, ParseException {
		User user = userRepo.getName(uid, token);
		HttpStatus httpStatus = (user != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(user, httpStatus);
	}

	@GetMapping("/validateEmailToken/{uId}/{token}")
	public RedirectView resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token, RedirectAttributes attributes) throws IOException, ParseException {
		HttpStatus httpStatus = tokenClass.checkToken(uId, token);

		if (httpStatus == HttpStatus.OK) {
			attributes.addAttribute("uid", uId);
			attributes.addAttribute("token", token);
			return new RedirectView("/newpassword");
		}
		return null;
	}

	@GetMapping("/validateMessageToken/{uId}/{token}")
	public ResponseEntity<HttpStatus> resetPassMessage(@PathVariable("uId") String uId, @PathVariable("token") String token) throws IOException, ParseException {
		return new ResponseEntity<>(tokenClass.checkToken(uId, token));
	}

}

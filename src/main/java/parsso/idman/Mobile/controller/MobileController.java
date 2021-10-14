package parsso.idman.Mobile.controller;


import com.google.zxing.WriterException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Mobile.RepoImpls.ServicesRepoImpl;
import parsso.idman.Models.Logs.Event;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Users.User;
import parsso.idman.repos.LogsRepo;
import parsso.idman.repos.ServiceRepo;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
public class MobileController {
	@Value(value = "${base.url}")
	private String url;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ServicesRepoImpl servicesRepo;
	@Autowired
	private LogsRepo.EventRepo eventRepo;
	@Autowired
	private ServiceRepo serviceRepo;
	@Autowired
	private parsso.idman.Helpers.User.Operations operations;

	@GetMapping(value = "/api/mobile/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody
	byte[] GetQr(HttpServletRequest request) throws IOException, WriterException {
		Principal principal = request.getUserPrincipal();
		User user = userRepo.retrieveUsers(principal.getName());
		String temp = url + "/api/mobile/sendsms" + "," + user.getUserId() + "," + user.getUsersExtraInfo().getQrToken();

		return servicesRepo.getQRCodeImage(temp, 500, 500);
	}

	@PostMapping("/api/mobile/mobnumber")
	public @ResponseBody
	ResponseEntity<String> mobileNumber(@RequestParam("uid") String uid, @RequestParam("qrToken") String QrToken) {
		User user = userRepo.retrieveUsers(uid);

		if (QrToken.equals(user.getUsersExtraInfo().getQrToken())) {

			return new ResponseEntity<>(user.getMobile(), HttpStatus.OK);
		} else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/api/mobile/sendsms")
	public @ResponseBody
	ResponseEntity<HttpStatus> sendSMS(@RequestParam("uid") String uid, @RequestParam("qrToken") String QrToken) {
		User user = userRepo.retrieveUsers(uid);

		if (QrToken.equals(user.getUsersExtraInfo().getQrToken())) {
			servicesRepo.ActivationSendMessage(user);
			return new ResponseEntity<>(HttpStatus.OK);
		} else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/api/mobile/active")
	public @ResponseBody
	ResponseEntity<JSONObject> active(@RequestParam("uid") String uid, @RequestParam("smsCode") String smsCode, @RequestParam("qrToken") String QrToken) throws IOException, org.json.simple.parser.ParseException {
		User user = userRepo.retrieveUsers(uid);

		JSONObject jsonObject = new JSONObject();

		if (QrToken.equals(user.getUsersExtraInfo().getQrToken()))
			if (servicesRepo.verifySMS(uid, smsCode).equals(HttpStatus.OK)) {
				jsonObject.put("mobileToken", user.getUsersExtraInfo().getMobileToken());
				jsonObject.put("UUID", operations.activeMobile(user));
				return new ResponseEntity<>(jsonObject, HttpStatus.OK);
			}
		return new ResponseEntity<>(jsonObject, HttpStatus.BAD_REQUEST);
	}

	//after activation

	@PostMapping("/api/mobile/services")
	public @ResponseBody
	ResponseEntity<List<MicroService>> M_listServices(@RequestParam("mobileToken") String MobileToken, @RequestParam("uid") String uid) throws IOException, org.json.simple.parser.ParseException {
		User user = userRepo.retrieveUsers(uid);
		if (MobileToken.equals(user.getUsersExtraInfo().getMobileToken()))
			return new ResponseEntity<>(serviceRepo.listUserServices(user), HttpStatus.OK);
		else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}

	@PostMapping("/api/mobile/events/{page}/{n}")
	public @ResponseBody
	ResponseEntity<Event.ListEvents> M_retrieveAllEvents(@RequestParam("mobileToken") String MobileToken,
	                                                     @PathVariable("page") String page, @PathVariable("n") String n,
	                                                     @RequestParam("uid") String uid) {

		User user = userRepo.retrieveUsers(uid);
		if (MobileToken.equals(user.getUsersExtraInfo().getMobileToken()))
			return new ResponseEntity<>(eventRepo.retrieveListSizeEvents(Integer.parseInt(page), Integer.parseInt(n)), HttpStatus.OK);
		else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}

	@PostMapping("/api/mobile/user")
	public @ResponseBody
	ResponseEntity<User> M_retrieveUser(@RequestParam("mobileToken") String MobileToken, @RequestParam("uid") String uid) {
		User user = userRepo.retrieveUsers(uid);
		if (MobileToken.equals(user.getUsersExtraInfo().getMobileToken()))
			return new ResponseEntity<>(user, HttpStatus.OK);
		else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}

	@PostMapping("/api/mobile/photo")
	public @ResponseBody
	ResponseEntity<byte[]> M_retrieveUserPhoto(@RequestParam("mobileToken") String MobileToken, @RequestParam("uid") String uid) {
		User user = userRepo.retrieveUsers(uid);
		if (MobileToken.equals(user.getUsersExtraInfo().getMobileToken()))
			return new ResponseEntity<>(userRepo.showProfilePic(user), HttpStatus.OK);
		else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}
}
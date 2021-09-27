package parsso.idman.controllers;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import parsso.idman.Helpers.Communicate.InstantMessage;
import parsso.idman.Helpers.Communicate.Token;
import parsso.idman.Helpers.User.ImportUsers;
import parsso.idman.Helpers.User.Operations;
import parsso.idman.Helpers.User.UsersExcelView;
import parsso.idman.Models.Users.ListUsers;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.RepoImpls.SystemRefreshRepoImpl;
import parsso.idman.Repos.EmailService;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class UsersController {
	final ImportUsers importUsers;
	final Operations operations;
	final EmailService emailService;
	// default sequence of variables which can be changed using frontend
	private final int[] defaultSequence = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
	private final InstantMessage instantMessage;
	private final UserRepo userRepo;
	@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
	@Autowired
	Token tokenClass;
	@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
	@Autowired
	UsersExcelView excelView;
	@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
	@Autowired
	SystemRefreshRepoImpl systemRefreshRepoImpl;

	public UsersController(UserRepo userRepo, InstantMessage instantMessage, EmailService emailService, Operations operations, ImportUsers importUsers) {
		this.userRepo = userRepo;
		this.instantMessage = instantMessage;
		this.emailService = emailService;
		this.operations = operations;
		this.importUsers = importUsers;
	}

	//************************************* APIs ****************************************

	@GetMapping("/api/user")
	public ResponseEntity<User> retrieveUser(HttpServletRequest request) throws IOException, ParseException {

		return new ResponseEntity<>(userRepo.retrieveUsers(request.getUserPrincipal().getName()), HttpStatus.OK);
	}

	@PutMapping("/api/user")
	public ResponseEntity<HttpStatus> updateUser(HttpServletRequest request, @RequestBody User user) throws IOException, ParseException {
		String userId = request.getUserPrincipal().getName();
		User userResult = userRepo.update(userId, userId, user);
		HttpStatus code = (userResult == null ? HttpStatus.FORBIDDEN : HttpStatus.OK);

		return new ResponseEntity<>(code);
	}

	@GetMapping("/api/user/photo")
	public ResponseEntity<String> getImage(HttpServletResponse response, HttpServletRequest request) throws IOException, ParseException {
		User user = userRepo.retrieveUsers(request.getUserPrincipal().getName());
		return new ResponseEntity<>(userRepo.showProfilePic(response, user), HttpStatus.OK);
	}

	@PostMapping("/api/user/photo")
	public RedirectView uploadProfilePic(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, ParseException {
		userRepo.uploadProfilePic(file, request.getUserPrincipal().getName());
		return new RedirectView("/dashboard");
	}

	@PutMapping("/api/user/password")
	public ResponseEntity<HttpStatus> changePassword(HttpServletRequest request,
	                                                 @RequestBody JSONObject jsonObject) throws IOException, ParseException {
		Principal principal = request.getUserPrincipal();
		String oldPassword = jsonObject.getAsString("currentPassword");
		String newPassword = jsonObject.getAsString("newPassword");
		String token = jsonObject.getAsString("token");
		if (jsonObject.getAsString("token") != null) token = jsonObject.getAsString("token");

		return new ResponseEntity<>(userRepo.changePassword(principal.getName(), oldPassword, newPassword, token));

	}

	@PutMapping("/api/users/password/expire")
	public ResponseEntity<List<String>> expirePassword(HttpServletRequest request,
	                                                   @RequestBody JSONObject jsonObject) {
		Principal principal = request.getUserPrincipal();
		List<String> preventedUsers = userRepo.expirePassword(principal.getName(), jsonObject);

		if (preventedUsers == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		else if (preventedUsers.size() == 0)
			return new ResponseEntity<>(HttpStatus.OK);
		else
			return new ResponseEntity<>(preventedUsers, HttpStatus.PARTIAL_CONTENT);

	}

	@GetMapping("/api/user/password/request")
	public ResponseEntity<Integer> requestSMS(HttpServletRequest request) throws IOException, ParseException {
		Principal principal = request.getUserPrincipal();
		User user = userRepo.retrieveUsers(principal.getName());
		int status = userRepo.requestToken(user);

		if (status > 0)
			return new ResponseEntity<>(status, HttpStatus.OK);
		else
			return new ResponseEntity<>(status, HttpStatus.FORBIDDEN);
	}
	//########### Users Section ###########

	@GetMapping("/api/users/u/{uid}")
	public ResponseEntity<User> retrieveUser(@PathVariable("uid") String userId) throws IOException, ParseException {
		User user = userRepo.retrieveUsers(userId);
		if (user == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		else return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/api/users/license/u/{uid}")
	public ResponseEntity<User> retrieveUserLicense(@PathVariable("uid") String userId) throws IOException, ParseException {
		User user = userRepo.retrieveUsersWithLicensed(userId);
		if (user == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		else return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/api/users")
	public ResponseEntity<List<UsersExtraInfo>> retrieveUsersMain() {
		return new ResponseEntity<>(userRepo.retrieveUsersMain(-1, -1), HttpStatus.OK);
	}

	@GetMapping("/api/users/group/{groupId}")
	public ResponseEntity<ListUsers> retrieveUsersMainWithGroupId(@PathVariable(name = "groupId") String groupId,
	                                                              @RequestParam(name = "page", defaultValue = "1") int page,
	                                                              @RequestParam(name = "nRec", defaultValue = "90000") int nRec) {
		ListUsers users = userRepo.retrieveUsersMainWithGroupId(groupId, page, nRec);
		if (users == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PutMapping("/api/users/group/{groupId}")
	public ResponseEntity<HttpStatus> massUsersGroupUpdate(HttpServletRequest request,
	                                                       @RequestBody JSONObject gu,
	                                                       @PathVariable(name = "groupId") String groupId) throws IOException, ParseException {
		return new ResponseEntity<>(userRepo.massUsersGroupUpdate(request.getUserPrincipal().getName(), groupId, gu));
	}

	@GetMapping("/api/users/{page}/{n}")
	public ResponseEntity<ListUsers> retrieveUsersMain(@PathVariable("page") int page, @PathVariable("n") int n,
	                                                   @RequestParam(name = "sortType", defaultValue = "") String sortType,
	                                                   @RequestParam(name = "groupFilter", defaultValue = "") String groupFilter,
	                                                   @RequestParam(name = "searchUid", defaultValue = "") String searchUid,
	                                                   @RequestParam(name = "userStatus", defaultValue = "") String userStatus,
	                                                   @RequestParam(name = "searchDisplayName", defaultValue = "") String searchDisplayName) {
		return new ResponseEntity<>(userRepo.retrieveUsersMain(page, n, sortType, groupFilter, searchUid, searchDisplayName, userStatus), HttpStatus.OK);
	}

	@GetMapping("/api/users/full")
	public ResponseEntity<List<User>> retrieveUsers() {
		if (userRepo.retrieveUsersFull().size() == 0) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		else return new ResponseEntity<>(userRepo.retrieveUsersFull(), HttpStatus.OK);
	}

	@PostMapping("/api/users")
	public ResponseEntity<JSONObject> bindLdapUser(HttpServletRequest request, @RequestBody User user) throws IOException, ParseException {
		JSONObject jsonObject = userRepo.create(request.getUserPrincipal().getName(), user);

		if (jsonObject == null || jsonObject.size() == 0)
			return new ResponseEntity<>(null, HttpStatus.OK);
		else return new ResponseEntity<>(jsonObject, HttpStatus.FOUND);

	}

	@PutMapping("/api/users/u/{uId}")
	public ResponseEntity<String> rebindLdapUser(HttpServletRequest request, @PathVariable("uId") String uid, @RequestBody User user) throws IOException, ParseException {

		User userResult = userRepo.update(request.getUserPrincipal().getName(), uid, user);
		return new ResponseEntity<>((userResult == null ? HttpStatus.FORBIDDEN : HttpStatus.OK));

	}

	@DeleteMapping("/api/users")
	public ResponseEntity<List<String>> unbindAllLdapUser(HttpServletRequest request, @RequestBody JSONObject jsonObject) throws IOException, ParseException {
		Principal principal = request.getUserPrincipal();
		List<String> names = userRepo.remove(principal.getName(), jsonObject);
		if (names.size() == 0)
			return new ResponseEntity<>(HttpStatus.OK);
		else
			return new ResponseEntity<>(names, HttpStatus.PARTIAL_CONTENT);
	}

	@PutMapping("/api/users/enable/u/{id}")
	public ResponseEntity<HttpStatus> enable(HttpServletRequest request, @PathVariable("id") String uid) throws IOException, ParseException {
		Principal principal = request.getUserPrincipal();
		return new ResponseEntity<>(operations.enable(principal.getName(), uid));
	}

	@PutMapping("/api/users/disable/u/{id}")
	public ResponseEntity<HttpStatus> disable(HttpServletRequest request, @PathVariable("id") String uid) throws IOException, ParseException {
		Principal principal = request.getUserPrincipal();
		return new ResponseEntity<>(operations.disable(principal.getName(), uid));
	}

	@PutMapping("/api/users/unlock/u/{id}")
	public ResponseEntity<HttpStatus> lockUnlock(HttpServletRequest request, @PathVariable("id") String uid) throws IOException, ParseException {
		Principal principal = request.getUserPrincipal();
		return new ResponseEntity<>(operations.unlock(principal.getName(), uid));
	}

	@PostMapping("/api/users/import")
	public ResponseEntity<JSONObject> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException, ParseException {

		JSONObject jsonObject = importUsers.importFileUsers(request.getUserPrincipal().getName(), file, defaultSequence, true);
		if (Integer.parseInt(jsonObject.getAsString("nUnSuccessful")) == 0)
			return new ResponseEntity<>(jsonObject, HttpStatus.OK);
		else return new ResponseEntity<>(jsonObject, HttpStatus.FOUND);
	}

	@PutMapping("/api/users/import/massUpdate")
	public ResponseEntity<JSONObject> updateConflicts(HttpServletRequest request, @RequestBody List<User> users) {
		return new ResponseEntity<>(userRepo.massUpdate(request.getUserPrincipal().getName(), users), HttpStatus.OK);
	}

	@PostMapping("/api/users/sendMail")
	public ResponseEntity<HttpStatus> sendMultipleMailByAdmin(@RequestBody JSONObject jsonObject) throws IOException, ParseException {
		return new ResponseEntity<>(emailService.sendMail(jsonObject), HttpStatus.OK);
	}

	@GetMapping("/api/users/export")
	public ModelAndView downloadExcel() {

		//noinspection ConstantConditions
		return new ModelAndView(excelView, "listUsers", null);
	}

	@GetMapping("/api/users/sync")
	public ResponseEntity<HttpStatus> simpleUsersSync() {
		return new ResponseEntity<>(userRepo.syncUsersDBs());
	}

	@PutMapping("/api/users/ou/{ou}")
	public ResponseEntity<List<String>> addGroups(HttpServletRequest request, @RequestParam("file") MultipartFile file, @PathVariable("ou") String ou) throws IOException, ParseException {
		List<String> notExist = userRepo.addGroupToUsers(request.getUserPrincipal().getName(), file, ou);
		if (ou.equals("none"))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		if (notExist == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		if (notExist.size() == 0)
			return new ResponseEntity<>(HttpStatus.OK);
		else
			return new ResponseEntity<>(notExist, HttpStatus.PARTIAL_CONTENT);
	}

	//########### Public Controllers ###########

	@GetMapping("/api/public/sendMail/{email}/{uid}/{cid}/{answer}")
	public ResponseEntity<Integer> sendMail(@PathVariable("email") String email,
	                                        @PathVariable("uid") String uid,
	                                        @PathVariable("cid") String cid,
	                                        @PathVariable("answer") String answer) throws IOException, ParseException {
		int time = userRepo.sendEmail(email, uid, cid, answer);
		return getIntegerResponseEntity(time);
	}

	@GetMapping("/api/public/sendMail/{email}/{cid}/{answer}")
	public ResponseEntity<Integer> sendMail(@PathVariable("email") String email,
	                                        @PathVariable("cid") String cid,
	                                        @PathVariable("answer") String answer) throws IOException, ParseException {

		int time = userRepo.sendEmail(email, null, cid, answer);
		return getIntegerResponseEntity(time);

	}

	private ResponseEntity<Integer> getIntegerResponseEntity(int time) {
		if (time > 0) {
			return new ResponseEntity<>(time, HttpStatus.OK);
		} else if (time == -1)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		else if (time == -2)
			return new ResponseEntity<>(HttpStatus.FOUND);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/public/sendSMS/{mobile}/{cid}/{answer}")
	public ResponseEntity<JSONObject> sendMessage(@PathVariable("mobile") String mobile,
	                                              @PathVariable("cid") String cid,
	                                              @PathVariable("answer") String answer) throws IOException, ParseException {

		int time = instantMessage.sendMessage(mobile, cid, answer);

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

	@GetMapping("/api/public/sendSMS/{mobile}/{uid}/{cid}/{answer}")
	public ResponseEntity<JSONObject> sendMessage(@PathVariable("mobile") String mobile,
	                                              @PathVariable("uid") String uid,
	                                              @PathVariable("cid") String cid,
	                                              @PathVariable("answer") String answer) throws IOException, ParseException {
		int time = instantMessage.sendMessage(mobile, uid, cid, answer);

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
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/public/sendTokenUser/{uid}/{cid}/{answer}")
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

	@PutMapping("/api/public/resetPass/{uid}/{token}")
	public ResponseEntity<HttpStatus> rebindLdapUser(@RequestParam("newPassword") String newPassword, @PathVariable("token") String token,
	                                                 @PathVariable("uid") String uid) throws IOException, ParseException {
		return new ResponseEntity<>(userRepo.resetPassword(uid, newPassword, token));
	}

	@GetMapping("/api/public/checkMail/{email}")
	public HttpEntity<List<JSONObject>> checkMail(@PathVariable("email") String email) {
		return new ResponseEntity<>(emailService.checkMail(email), HttpStatus.OK);
	}

	@GetMapping("/api/public/checkMobile/{mobile}")
	public HttpEntity<List<JSONObject>> checkMobile(@PathVariable("mobile") String mobile) {
		return new ResponseEntity<>(instantMessage.checkMobile(mobile), HttpStatus.OK);
	}

	@GetMapping("/api/public/getName/{uid}/{token}")
	public ResponseEntity<User> getName(@PathVariable("uid") String uid, @PathVariable("token") String token) throws IOException, ParseException {
		User user = userRepo.getName(uid, token);
		HttpStatus httpStatus = (user != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(user, httpStatus);
	}

	@GetMapping("/api/public/validateEmailToken/{uId}/{token}")
	public RedirectView resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token, RedirectAttributes attributes) throws IOException, ParseException {
		HttpStatus httpStatus = tokenClass.checkToken(uId, token);

		if (httpStatus == HttpStatus.OK) {
			attributes.addAttribute("uid", uId);
			attributes.addAttribute("token", token);
			return new RedirectView("/newpassword");
		}
		return null;
	}

	@GetMapping("/api/public/validateMessageToken/{uId}/{token}")
	public ResponseEntity<HttpStatus> resetPassMessage(@PathVariable("uId") String uId, @PathVariable("token") String token) throws IOException, ParseException {
		return new ResponseEntity<>(tokenClass.checkToken(uId, token));
	}

}
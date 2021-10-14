package parsso.idman.controllers.ok.users;

import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import parsso.idman.Helpers.User.ImportUsers;
import parsso.idman.Helpers.User.Operations;
import parsso.idman.Helpers.User.UsersExcelView;
import parsso.idman.Models.Users.ListUsers;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Repos.email.EmailService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/api/users/")
public class Users {
	final ImportUsers importUsers;
	final Operations operations;
	final EmailService emailService;
	// default sequence of variables which can be changed using frontend
	private final int[] defaultSequence = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
	private final UserRepo userRepo;
	private final UsersExcelView excelView;

	@Autowired
	public Users(UserRepo userRepo, EmailService emailService, Operations operations, UsersExcelView excelView, ImportUsers importUsers) {
		this.userRepo = userRepo;
		this.emailService = emailService;
		this.operations = operations;
		this.importUsers = importUsers;
		this.excelView = excelView;
	}

	@PutMapping("/password/expire")
	public ResponseEntity<List<String>> expirePassword(HttpServletRequest request,
													   @RequestBody JSONObject jsonObject) throws IOException, ParseException {
		Principal principal = request.getUserPrincipal();
		List<String> preventedUsers = userRepo.expirePassword(principal.getName(), jsonObject);

		if (preventedUsers == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		else if (preventedUsers.size() == 0)
			return new ResponseEntity<>(HttpStatus.OK);
		else
			return new ResponseEntity<>(preventedUsers, HttpStatus.PARTIAL_CONTENT);

	}

	@GetMapping("/u/{uid}")
	public ResponseEntity<User> retrieveUser(@PathVariable("uid") String userId) throws IOException, ParseException {
		User user = userRepo.retrieveUsers(userId);
		if (user == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		else return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/license/u/{uid}")
	public ResponseEntity<User> retrieveUserLicense(@PathVariable("uid") String userId) throws IOException, ParseException {
		User user = userRepo.retrieveUsersWithLicensed(userId);
		if (user == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		else return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<UsersExtraInfo>> retrieveUsersMain() {
		return new ResponseEntity<>(userRepo.retrieveUsersMain(-1, -1), HttpStatus.OK);
	}

	@GetMapping("/{page}/{n}")
	public ResponseEntity<ListUsers> retrieveUsersMain(@PathVariable("page") int page, @PathVariable("n") int n,
													   @RequestParam(name = "sortType", defaultValue = "") String sortType,
													   @RequestParam(name = "groupFilter", defaultValue = "") String groupFilter,
													   @RequestParam(name = "searchUid", defaultValue = "") String searchUid,
													   @RequestParam(name = "userStatus", defaultValue = "") String userStatus,
													   @RequestParam(name = "searchDisplayName", defaultValue = "") String searchDisplayName) {
		return new ResponseEntity<>(userRepo.retrieveUsersMain(page, n, sortType, groupFilter, searchUid, searchDisplayName, userStatus), HttpStatus.OK);
	}


	@GetMapping("/group/{groupId}")
	public ResponseEntity<ListUsers> retrieveUsersMainWithGroupId(@PathVariable(name = "groupId") String groupId,
	                                                              @RequestParam(name = "page", defaultValue = "1") int page,
	                                                              @RequestParam(name = "nRec", defaultValue = "90000") int nRec) {
		ListUsers users = userRepo.retrieveUsersMainWithGroupId(groupId, page, nRec);
		if (users == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PutMapping("/group/{groupId}")
	public ResponseEntity<HttpStatus> massUsersGroupUpdate(HttpServletRequest request,
	                                                       @RequestBody JSONObject gu,
	                                                       @PathVariable(name = "groupId") String groupId) throws IOException, ParseException {
		return new ResponseEntity<>(userRepo.massUsersGroupUpdate(request.getUserPrincipal().getName(), groupId, gu));
	}

	@PostMapping
	public ResponseEntity<JSONObject> bindLdapUser(HttpServletRequest request, @RequestBody User user) throws IOException, ParseException {
		JSONObject jsonObject = userRepo.create(request.getUserPrincipal().getName(), user);

		if (jsonObject == null || jsonObject.size() == 0)
			return new ResponseEntity<>(null, HttpStatus.OK);
		else return new ResponseEntity<>(jsonObject, HttpStatus.FOUND);

	}

	@PutMapping("/u/{uId}")
	public ResponseEntity<String> rebindLdapUser(HttpServletRequest request, @PathVariable("uId") String uid, @RequestBody User user) throws IOException, ParseException {

		User userResult = userRepo.update(request.getUserPrincipal().getName(), uid, user);
		return new ResponseEntity<>((userResult == null ? HttpStatus.FORBIDDEN : HttpStatus.OK));

	}

	@DeleteMapping
	public ResponseEntity<List<String>> unbindAllLdapUser(HttpServletRequest request, @RequestBody JSONObject jsonObject) throws IOException, ParseException {
		Principal principal = request.getUserPrincipal();
		List<String> names = userRepo.remove(principal.getName(), jsonObject);
		if (names.size() == 0)
			return new ResponseEntity<>(HttpStatus.OK);
		else
			return new ResponseEntity<>(names, HttpStatus.PARTIAL_CONTENT);
	}

	@PutMapping("/operation")
	public ResponseEntity<HttpStatus> operation(HttpServletRequest request,
												@RequestParam("id") String uid,
												@RequestParam("operation") String operation) throws IOException, ParseException {
		switch (operation){
			case "unlock":
				return new ResponseEntity<>(operations.unlock(request.getUserPrincipal().getName(), uid));
			case "enable":
				return new ResponseEntity<>(operations.enable(request.getUserPrincipal().getName(), uid));
			case "disable":
				return new ResponseEntity<>(operations.disable(request.getUserPrincipal().getName(), uid));
			default:
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		}

	}

	@PostMapping("/sendMail")
	public ResponseEntity<HttpStatus> sendMultipleMailByAdmin(@RequestBody JSONObject jsonObject) throws IOException, ParseException {
		return new ResponseEntity<>(emailService.sendMail(jsonObject), HttpStatus.OK);
	}

	@PostMapping("/import")
	public ResponseEntity<JSONObject> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException, ParseException {

		JSONObject jsonObject = importUsers.importFileUsers(request.getUserPrincipal().getName(), file, defaultSequence, true);
		if (Integer.parseInt(jsonObject.getAsString("nUnSuccessful")) == 0)
			return new ResponseEntity<>(jsonObject, HttpStatus.OK);
		else return new ResponseEntity<>(jsonObject, HttpStatus.FOUND);
	}

	@PutMapping("/import/massUpdate")
	public ResponseEntity<JSONObject> updateConflicts(HttpServletRequest request, @RequestBody List<User> users) {
		return new ResponseEntity<>(userRepo.massUpdate(request.getUserPrincipal().getName(), users), HttpStatus.OK);
	}

	@GetMapping("/export")
	public ModelAndView downloadExcel() {

		return new ModelAndView(excelView, "listUsers", Object.class);
	}

	@PutMapping("/ou/{ou}")
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

}

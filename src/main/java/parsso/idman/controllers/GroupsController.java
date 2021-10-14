package parsso.idman.controllers;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Groups.Group;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.repos.GroupRepo;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequestMapping("/api/groups")
@Controller
public class GroupsController {
	private final GroupRepo groupRepo;
	private final UserRepo userRepo;

	public GroupsController(UserRepo userRepo, GroupRepo groupRepo) {
		this.userRepo = userRepo;
		this.groupRepo = groupRepo;

	}

	@GetMapping("/user")
	public ResponseEntity<List<Group>> retrieveUserOU(HttpServletRequest request) throws IOException, ParseException {
		User user = userRepo.retrieveUsers(request.getUserPrincipal().getName());
		List<Group> groups = groupRepo.retrieveCurrentUserGroup(user);
		groups.removeAll(Collections.singleton(null));
		return new ResponseEntity<>(groups, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<HttpStatus> bindLdapGroup(HttpServletRequest request, @RequestBody Group ou) throws IOException, ParseException {
		return new ResponseEntity<>(groupRepo.create(request.getUserPrincipal().getName(), ou));
	}

	@PutMapping("/{id}")
	public ResponseEntity<HttpStatus> rebindLdapUser(HttpServletRequest request, @RequestBody Group ou, @PathVariable("id") String id) throws IOException, ParseException {

		return new ResponseEntity<>(groupRepo.update(request.getUserPrincipal().getName(), id, ou));
	}

	@GetMapping
	public ResponseEntity<?> retrieveGroups(@RequestParam(value = "id", defaultValue = "") String id) throws IOException, ParseException {
		if (!id.equals(""))
			return new ResponseEntity<>(groupRepo.retrieveOu(false, id), HttpStatus.OK);
		else{
			List<Group> groups = groupRepo.retrieve();
			groups.removeIf(t -> t.getName() == null);
			return new ResponseEntity<>(groups, HttpStatus.OK);
		}
	}

	@DeleteMapping
	public ResponseEntity<HttpStatus> unbindAllLdapOU(HttpServletRequest request, @RequestBody JSONObject jsonObject) throws IOException, ParseException {
		return new ResponseEntity<>(groupRepo.remove(request.getUserPrincipal().getName(), jsonObject));
	}

	@PutMapping("/password/expire")
	public ResponseEntity<String> expireUsersGroupPassword(HttpServletRequest request,
												   @RequestBody JSONObject jsonObject,
												   @RequestParam(value = "id", defaultValue = "")String id) throws IOException, ParseException {

		String userId = request.getUserPrincipal().getName();

		if(!id.equals(""))
				return new ResponseEntity(userRepo.expirePassword(userId, jsonObject), HttpStatus.OK);
			else
				return expireUsersSpecGroupPassword(request, id);

	}
	private ResponseEntity<String> expirePassword(String name, JSONObject jsonObject) throws IOException, ParseException {

		List<String> preventedUsers = userRepo.expirePassword(name, jsonObject);

			if (preventedUsers == null)
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			else if (preventedUsers.size() == 0)
				return new ResponseEntity<>(HttpStatus.OK);
			else
				return new ResponseEntity<>((MultiValueMap<String, String>) preventedUsers, HttpStatus.PARTIAL_CONTENT);
	}

	public ResponseEntity<String> expireUsersSpecGroupPassword(HttpServletRequest request, String gid) throws IOException, ParseException {

		ArrayList<String> temp = new ArrayList<>();
		JSONObject jsonObject = new JSONObject();
		List<UsersExtraInfo> users = userRepo.retrieveGroupsUsers(gid);
		for (UsersExtraInfo usersExtraInfo : users) {
			temp.add(usersExtraInfo.getUserId());
		}
		jsonObject.put("names", temp);
		return expirePassword(request.getUserPrincipal().getName(), jsonObject);
	}

}

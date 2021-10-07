package parsso.idman.RepoImpls;


import net.minidev.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.UniformLogger;
import parsso.idman.Helpers.User.BuildDnUser;
import parsso.idman.Helpers.User.ExpirePassword;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Groups.Group;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.logs.transcripts.TranscriptRepo;
import parsso.idman.Repos.UserRepo;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import java.io.IOException;
import java.util.*;

@Service
public class GroupRepoImpl implements GroupRepo {
	@Autowired
	BuildDnUser buildDnUser;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	UniformLogger uniformLogger;
	@Autowired
	ExpirePassword expirePassword;
	@Value("${spring.ldap.base.dn}")
	private String BASE_DN;
	@Autowired
	private LdapTemplate ldapTemplate;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ServiceRepo serviceRepo;
	@Autowired
	private TranscriptRepo transcriptRepo;

	@Override
	public HttpStatus remove(String doerID, JSONObject jsonObject) throws IOException, ParseException {

		ArrayList<String> jsonArray = (ArrayList<String>) jsonObject.get("names");
		DirContextOperations context;
		for (String s : jsonArray) {
			Group group = retrieveOu(false, s);

			Name dn = buildDn(group.getId());
			try {
				ldapTemplate.unbind(dn);

				uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, group.getId(), Variables.MODEL_GROUP,
						Variables.ACTION_REMOVE, Variables.RESULT_SUCCESS, ""));

			} catch (Exception e) {
				e.printStackTrace();
				uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_GROUP, group.getId(), Variables.MODEL_GROUP,
						Variables.ACTION_REMOVE, Variables.RESULT_FAILED, "writing to ldap"));
			}

			for (UsersExtraInfo user : userRepo.retrieveGroupsUsers(group.getId())) {
				if (user != null && user.getMemberOf() != null)
					for (String groupN : user.getMemberOf()) {
						if (groupN.equalsIgnoreCase(group.getId())) {
							context = ldapTemplate.lookupContext(buildDnUser.buildDn(user.getUserId()));
							context.removeAttributeValue("ou", group.getId());
							try {
								ldapTemplate.modifyAttributes(context);
								UsersExtraInfo simpleUser = mongoTemplate.findOne
										(new Query(Criteria.where("userId").is(user.getUserId())), UsersExtraInfo.class, Variables.col_usersExtraInfo);
								try {
									simpleUser.getMemberOf().remove(group.getId());
								}catch (Exception e) {
								e.printStackTrace();
								}

								mongoTemplate.remove
										(new Query(Criteria.where("userId").is(user.getUserId())), Variables.col_usersExtraInfo);

								mongoTemplate.save
										(simpleUser, Variables.col_usersExtraInfo);

								uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, user.getUserId(),
										Variables.MODEL_GROUP, Variables.ACTION_REMOVE, Variables.RESULT_SUCCESS, groupN + "Removing 'OU'=+" + groupN));


							} catch (Exception e) {
								e.printStackTrace();
								uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, user.getUserId(),
										Variables.MODEL_GROUP, Variables.ACTION_REMOVE, Variables.RESULT_FAILED, groupN, "Changing LDAP for removing 'OU'=" + groupN));

							}
						}

					}
			}
		}

		return HttpStatus.OK;
	}

	@Override
	public Group retrieveOu(boolean simple, String uid) throws IOException, ParseException {

		List<Group> groups = retrieve();

		for (Group group : groups) {
			if (!simple && group.getId().equalsIgnoreCase(uid)) {
				group.setService(transcriptRepo.servicesOfGroup(uid));
				return group;
			} else if (simple && group.getId().equalsIgnoreCase(uid))
				return group;
		}
		return null;
	}

	@Override
	public List<Group> retrieveCurrentUserGroup(User user) {
		List<String> memberOf = user.getMemberOf();
		List<Group> groups = new ArrayList<>();
		try {
			for (String s : memberOf) {
				groups.add(retrieveOu(false, s));
			}
		} catch (NullPointerException | IOException ignored) {

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return groups;
	}

	private Attributes buildAttributes(String uid, Group group) {

		BasicAttribute ocattr = new BasicAttribute("objectclass");
		ocattr.add("extensibleObject");
		ocattr.add("organizationalUnit");
		ocattr.add("top");
		ocattr.add("top");

		Attributes attrs = new BasicAttributes();
		attrs.put(ocattr);
		attrs.put("name", group.getName());
		attrs.put("ou", group.getId());
		if (!group.getDescription().equals(""))
			attrs.put("description", group.getDescription());
		else
			attrs.put("description", " ");

		return attrs;
	}

	public Name buildDn(String id) {
		return LdapNameBuilder.newInstance("ou=" + "Groups" + "," + BASE_DN).add("ou", id).build();
	}

	@Override
	public List<Group> retrieve() {
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

		final AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "organizationalUnit"));

		return ldapTemplate.search("ou=Groups," + BASE_DN, filter.encode(),
				new OUAttributeMapper());
	}

	@Override
	public HttpStatus create(String doerID, Group ou) throws IOException, ParseException {

		List<Group> groups = retrieve();

		for (Group group : groups)
			if (group.getId().equals(ou.getId()))
				return HttpStatus.FOUND;

		Name dn = buildDn(ou.getId());
		try {
			ldapTemplate.bind(dn, null, buildAttributes(ou.getId(), ou));

			uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, ou.getId(), Variables.MODEL_GROUP,
					Variables.ACTION_CREATE, Variables.RESULT_SUCCESS, ""));

			return HttpStatus.OK;

		} catch (Exception e) {
			e.printStackTrace();
			uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_GROUP, ou.getId(), Variables.MODEL_GROUP,
					Variables.ACTION_CREATE, Variables.RESULT_FAILED, "Writing to ldap"));

			return HttpStatus.BAD_REQUEST;
		}
	}

	@Override
	public HttpStatus update(String doerID, String id, Group ou) throws IOException, ParseException {

		Name dn = buildDn(id);

		List<Group> groups = retrieve();

		for (Group group : groups)
			if (!(id.equals(ou.getId())) && group.getId().equals(ou.getId()))
				return HttpStatus.FOUND;

		if (!(id.equals(ou.getId()))) {

			try {
				ldapTemplate.unbind(dn);
				create(doerID, ou);
				DirContextOperations contextUser;
				uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, id, Variables.MODEL_GROUP,
						Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ou, ""));

				for (UsersExtraInfo user : userRepo.retrieveGroupsUsers(id)) {
					for (String group : user.getMemberOf()) {
						if (group.equalsIgnoreCase(id)) {
							contextUser = ldapTemplate.lookupContext(buildDnUser.buildDn(user.getUserId()));
							contextUser.removeAttributeValue("ou", id);
							contextUser.addAttributeValue("ou", ou.getId());
							ldapTemplate.modifyAttributes(contextUser);
							UsersExtraInfo usersExtraInfo = mongoTemplate.findOne
									(new Query(Criteria.where("userId").is(user.getUserId())), UsersExtraInfo.class, Variables.col_usersExtraInfo);
							if (usersExtraInfo != null) usersExtraInfo.getMemberOf().remove(id);
							usersExtraInfo.getMemberOf().add(ou.getId());

							mongoTemplate.remove
									(new Query(Criteria.where("userId").is(user.getUserId())), Variables.col_usersExtraInfo);

							mongoTemplate.save
									(usersExtraInfo, Variables.col_usersExtraInfo);
						}
					}
				}

				List<parsso.idman.Models.Services.Service> services = serviceRepo.listServicesWithGroups(id);
				if (services != null)
					for (parsso.idman.Models.Services.Service service : services) {

						//remove old id and add new id
						boolean ou1 = ((List<String>) ((JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1)).remove(id);
						((List<String>) ((JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1)).add(ou.getId());

						// delete old service
						org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
						jsonObject.put("names", ((((JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1))));
						serviceRepo.deleteServices(doerID, jsonObject);

						// create new service

						serviceRepo.updateOuIdChange(doerID, service, service.getId(), service.getName(), id, ou.getId());

					}

				uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, id, "", Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ou, ""));

				return HttpStatus.OK;

			} catch (IOException ioException) {

				ioException.printStackTrace();
				uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_GROUP, id, "Group", "update", Variables.RESULT_FAILED, "ioException"));
				return HttpStatus.BAD_REQUEST;
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
				uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_GROUP, id, "Group", "update", Variables.RESULT_FAILED, "parsing"));
				return HttpStatus.BAD_REQUEST;
			}

		} else {

			try {
				ldapTemplate.rebind(dn, null, buildAttributes(ou.getId(), ou));
				uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, ou.getId(), "",
						Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ou, ""));

				return HttpStatus.OK;

			} catch (Exception e) {
				e.printStackTrace();
				uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_GROUP, doerID, ou.getId(), Variables.ACTION_UPDATE,
						Variables.RESULT_FAILED, "Writing to ldap"));
				return HttpStatus.BAD_REQUEST;
			}
		}


	}

	private class OUAttributeMapper implements AttributesMapper<Group> {
		@Override
		public Group mapFromAttributes(Attributes attributes) throws NamingException {
			Group group = new Group();

			group.setId(null != attributes.get("ou") ? attributes.get("ou").get().toString() : null);
			group.setName(null != attributes.get("name") ? attributes.get("name").get().toString() : null);
			group.setDescription(null != attributes.get("description") ? attributes.get("description").get().toString() : null);
			group.setUsersCount(mongoTemplate.count(new Query(Criteria.where("memberOf").is(group.getId())), Variables.col_usersExtraInfo));
			return group;
		}
	}
}

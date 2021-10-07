package parsso.idman.RepoImpls;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.UniformLogger;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Users.UserRole;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.RolesRepo;

import java.io.IOException;
import java.util.List;

@Service
public class RoleRepoImpl implements RolesRepo {
	final String collection = Variables.col_usersExtraInfo;
	String model = "Role";
	@Autowired
	UniformLogger uniformLogger;
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public List<UserRole> retrieve() {
		Query query = new Query();
		return mongoTemplate.find(query, UserRole.class, collection);
	}

	@Override
	public HttpStatus updateRole(String doerID, String role, JSONObject users) throws IOException, ParseException {
		int i = 0;
		List<String> userIDs = (List<String>) users.get("names");
		for (String userId : userIDs) {
			try {
				Query query = new Query(Criteria.where("userId").is(userId));
				UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, collection);
				String oldRole = usersExtraInfo.getRole();
				usersExtraInfo.setRole(role);
				mongoTemplate.save(usersExtraInfo, collection);
				uniformLogger.info(doerID, new ReportMessage(model, userId, "", "change", Variables.RESULT_SUCCESS,
						"from \"" + oldRole + "\" to \"" + role + "\""));

			} catch (Exception e) {
				i++;
				e.printStackTrace();
				uniformLogger.warn(doerID, new ReportMessage(model, userId, "", "change", Variables.RESULT_FAILED, "due to writing to ldap"));

			}

			if (i > 0) {
				uniformLogger.info(doerID, new ReportMessage(model, userId, "", "change", Variables.RESULT_SUCCESS, "partially done"));
				return HttpStatus.PARTIAL_CONTENT;

			}
		}
		return HttpStatus.OK;

	}
}

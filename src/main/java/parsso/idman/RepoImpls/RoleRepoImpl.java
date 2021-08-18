package parsso.idman.RepoImpls;


import net.minidev.json.JSONObject;
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
    public HttpStatus updateRole(String doerID, String role, JSONObject users) {
        int i = 0;
        List<String> userIDs = (List<String>) users.get("names");
        for (String userId : userIDs) {
            try {
                Query query = new Query(Criteria.where("userId").is(userId));
                UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, collection);
                String oldRole = usersExtraInfo.getRole();
                usersExtraInfo.setRole(role);
                mongoTemplate.save(usersExtraInfo, collection);
                uniformLogger.record(doerID, Variables.LEVEL_INFO, new ReportMessage(model, userId, "", "change", "success",
                        "from \"" + oldRole + "\" to \"" + role + "\""));

            } catch (Exception e) {
                i++;
                uniformLogger.record(doerID, Variables.LEVEL_WARN, new ReportMessage(model, userId, "", "change", "failed", "due to writing to ldap"));

            }

            if (i > 0) {
                uniformLogger.record(doerID, Variables.LEVEL_INFO, new ReportMessage(model, userId, "", "change", "success", "partially done"));
                return HttpStatus.PARTIAL_CONTENT;

            }
        }
        return HttpStatus.OK;

    }
}

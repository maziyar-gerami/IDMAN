package parsso.idman.repoImpls;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.UserRole;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.RolesRepo;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
@Service
public class RoleRepoImpl implements RolesRepo {
    final String collection = Variables.col_usersExtraInfo;
    final String model = "Role";
    @Autowired
    UniformLogger uniformLogger;
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<UserRole> retrieve() {
        Query query = new Query();
        try {
            return mongoTemplate.find(query, UserRole.class, collection);

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public HttpStatus updateRole(String doerID, String role, JSONObject users) {
        int i = 0;
        List<String> userIDs = (List<String>) users.get("names");
        for (String userId : userIDs) {
            try {
                Query query = new Query(Criteria.where("_id").is(userId));
                UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, collection);
                String oldRole = Objects.requireNonNull(usersExtraInfo).getRole();
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

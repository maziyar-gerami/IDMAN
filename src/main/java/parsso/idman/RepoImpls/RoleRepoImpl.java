package parsso.idman.RepoImpls;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Users.UserRole;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.RolesRepo;

import java.util.List;

@Service
public class RoleRepoImpl implements RolesRepo {

    final String collection = "IDMAN_UsersExtraInfo";

    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public List<UserRole> retrieve() {
        Query query = new Query();
        return mongoTemplate.find(query,UserRole.class,collection);
    }

    @Override
    public HttpStatus updateRole(String role, JSONObject users) {
        List<String> userIDs = (List<String>) users.get("names");
        for (String userId:userIDs) {
            try {
                Query query = new Query(Criteria.where("userId").is(userId));
                UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class,collection);
                usersExtraInfo.setRole(role);
                mongoTemplate.save(usersExtraInfo,collection);
            }catch (Exception e){
                return  HttpStatus.PARTIAL_CONTENT;

            }
        }
        return HttpStatus.OK;

    }
}

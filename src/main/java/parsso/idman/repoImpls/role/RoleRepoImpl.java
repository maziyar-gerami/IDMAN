package parsso.idman.repoImpls.role;


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
import parsso.idman.models.users.User;
import parsso.idman.models.users.User.UserRole;
import parsso.idman.repoImpls.role.subclass.Retrieve;
import parsso.idman.repoImpls.role.subclass.Update;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.RolesRepo;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
@Service
public class RoleRepoImpl implements RolesRepo {
    final String collection = Variables.col_usersExtraInfo;
    final UniformLogger uniformLogger;
    final MongoTemplate mongoTemplate;

    @Autowired
    public RoleRepoImpl(UniformLogger uniformLogger, MongoTemplate mongoTemplate) {
        this.uniformLogger = uniformLogger;
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<User.UserRole> retrieve() {
        return new Retrieve(mongoTemplate).retrieve();
    }

    @Override
    public HttpStatus updateRole(String doerID, String role, JSONObject users) {
        return new Update(mongoTemplate, uniformLogger).update(doerID, role, users);
    }
}

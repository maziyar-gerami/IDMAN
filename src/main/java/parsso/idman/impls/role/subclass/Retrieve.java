package parsso.idman.impls.role.subclass;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import parsso.idman.helpers.Variables;
import parsso.idman.models.users.User;
import parsso.idman.models.users.User.UserRole;

public class Retrieve {
  MongoTemplate mongoTemplate;

  public Retrieve(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public List<User.UserRole> retrieve() {
    try {
      return mongoTemplate.find(new Query(), UserRole.class, Variables.col_usersExtraInfo);

    } catch (Exception e) {
      return null;
    }
  }
}
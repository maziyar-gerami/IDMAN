package parsso.idman.impls.TokenManagement.subclasses;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import parsso.idman.helpers.Variables;

public class ValidateToken {
  final MongoTemplate mongoTemplate;

  public ValidateToken(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public boolean valid(String userId, String token) {
    Query query = new Query(Criteria.where("username").is(userId).andOperator(Criteria.where("token").is(token)));
    return mongoTemplate.count(query, Variables.col_Token) > 0;
  }

}

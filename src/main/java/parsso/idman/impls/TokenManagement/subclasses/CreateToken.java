package parsso.idman.impls.TokenManagement.subclasses;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;

import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Token;

public class CreateToken {
  final MongoTemplate mongoTemplate;

  public CreateToken(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public HttpStatus create(String username, String token) {
    if (!mongoTemplate.collectionExists(Variables.col_Token)) {
      try {
        mongoTemplate.createCollection(Variables.col_Token);
      } catch (Exception e) {
        return HttpStatus.FORBIDDEN;
      }
    }
    Token tokenTemp = new Token(username, token);
    try {
      mongoTemplate.save(tokenTemp, Variables.col_Token);
      return HttpStatus.CREATED;
    } catch (Exception e) {
      return HttpStatus.BAD_REQUEST;
    }
  }

}

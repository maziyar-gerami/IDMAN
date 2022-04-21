package parsso.idman.impls.TokenManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import parsso.idman.impls.TokenManagement.subclasses.CreateToken;
import parsso.idman.impls.TokenManagement.subclasses.DeleteToken;
import parsso.idman.impls.TokenManagement.subclasses.RetrieveToken;
import parsso.idman.impls.TokenManagement.subclasses.ValidateToken;
import parsso.idman.repos.TokenManagementRepo;

@Service
public class TokenManagementRepoImpl implements TokenManagementRepo {
  final MongoTemplate mongoTemplate;

  @Autowired
  TokenManagementRepoImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public String retrieve(String userId) {
    return new RetrieveToken(mongoTemplate).retrieve(userId);
  }

  @Override
  public HttpStatus delete(String username, String token) {
    return new DeleteToken(mongoTemplate).delete(username, token);
  }

  @Override
  public HttpStatus delete(String username) {
    return new DeleteToken(mongoTemplate).delete(username);
  }

  @Override
  public HttpStatus create(String username, String token) {
    return new CreateToken(mongoTemplate).create(username, token);
  }

  @Override
  public boolean valid(String userId, String token) {
    return new ValidateToken(mongoTemplate).valid(userId, token);
  }
}

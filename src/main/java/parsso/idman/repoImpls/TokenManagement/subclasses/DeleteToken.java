package parsso.idman.repoImpls.TokenManagement.subclasses;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;

import parsso.idman.helpers.Variables;

public class DeleteToken {

    final MongoTemplate mongoTemplate;

    public DeleteToken(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    public HttpStatus delete(String username, String token) {
        Query query = new Query(Criteria.where("username").is(username).andOperator(Criteria.where("token").is(token)));
        try {
            mongoTemplate.remove(query, Variables.col_Token);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public HttpStatus delete(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        try {
            mongoTemplate.remove(query, Variables.col_Token);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

}

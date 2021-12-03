package parsso.idman.repoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Token;
import parsso.idman.repos.TokenManagementRepo;
import org.springframework.data.mongodb.core.query.Query;


@Service
public class TokenManagementRepoImpl implements TokenManagementRepo {
    MongoTemplate mongoTemplate;
    @Autowired
    TokenManagementRepoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate=mongoTemplate;
    }
    @Override
    public String retrieve(String userId) {
        return null;
    }

    @Override
    public HttpStatus delete(String userName, String token) {
        Query query = new Query(Criteria.where("username").is(userName).andOperator(Criteria.where("token").is(token)));
        try {
            mongoTemplate.remove(query, Variables.col_Token);
            return HttpStatus.OK;
        }catch (Exception e){
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus delete(String userName) {
        Query query = new Query(Criteria.where("username").is(userName));
        try {
            mongoTemplate.remove(query, Variables.col_Token);
            return HttpStatus.OK;
        }catch (Exception e){
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus create(String userName, String token) {
        if (!mongoTemplate.collectionExists(Variables.col_Token)) {
            try {
                mongoTemplate.createCollection(Variables.col_Token);
            }catch (Exception e){
                return HttpStatus.FORBIDDEN;
            }
        }
        Token tokenTemp = new Token(userName,token);
        try {
            mongoTemplate.save(tokenTemp, Variables.col_Token);
            return HttpStatus.CREATED;
        }catch (Exception e){
            return HttpStatus.BAD_REQUEST;
        }
    }
}

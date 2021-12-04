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

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Service
public class TokenManagementRepoImpl implements TokenManagementRepo {
    MongoTemplate mongoTemplate;
    @Autowired
    TokenManagementRepoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate=mongoTemplate;
    }
    @Override
    public String retrieve(String userId) {
        List<Token> tokens = mongoTemplate.find(new Query(Criteria.where("username").is(userId)),Token.class,Variables.col_Token);
        return tokens.get(0).getToken();
    }

    @Override
    public HttpStatus delete(String username, String token) {
        Query query = new Query(Criteria.where("username").is(username).andOperator(Criteria.where("token").is(token)));
        try {
            mongoTemplate.remove(query, Variables.col_Token);
            return HttpStatus.OK;
        }catch (Exception e){
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus delete(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        try {
            mongoTemplate.remove(query, Variables.col_Token);
            return HttpStatus.OK;
        }catch (Exception e){
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus create(String username, String token) {
        if (!mongoTemplate.collectionExists(Variables.col_Token)) {
            try {
                mongoTemplate.createCollection(Variables.col_Token);
            }catch (Exception e){
                return HttpStatus.FORBIDDEN;
            }
        }
        Token tokenTemp = new Token(username,token);
        try {
            mongoTemplate.save(tokenTemp, Variables.col_Token);
            return HttpStatus.CREATED;
        }catch (Exception e){
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public boolean valid(String userId, String token) {
        Query query = new Query(Criteria.where("username").is(userId).andOperator(Criteria.where("token").is(token)));
        return mongoTemplate.count(query,Variables.col_Token)>0;
    }
}

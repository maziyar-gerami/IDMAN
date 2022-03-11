package parsso.idman.repoImpls.TokenManagement.subclasses;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Token;
public class RetrieveToken {
    final MongoTemplate mongoTemplate;

    public RetrieveToken(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public String retrieve(String userId) {
        List<Token> tokens = mongoTemplate.find(new Query(Criteria.where("username").is(userId)), Token.class, Variables.col_Token);

        return tokens.get(tokens.size() - 1).getToken();
    }
}

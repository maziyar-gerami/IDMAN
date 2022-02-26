package parsso.idman.repoImpls.users.passwordOprations;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.user.ExpirePassword;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.UserRepo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class PasswordOpRepoImpl implements UserRepo.PasswordOp {
    final MongoTemplate mongoTemplate;
    final UniformLogger uniformLogger;
    final UserRepo.UsersOp.Retrieve usersOpRetrieve;
    final LdapTemplate ldapTemplate;
    final Token tokenClass;
    final ExpirePassword expirePassword;
    final UserRepo.Supplementary supplementary;

    @Value("${base.url}")
    private String BASE_URL;

    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;

    @Autowired
    public PasswordOpRepoImpl(MongoTemplate mongoTemplate, UniformLogger uniformLogger, UserRepo.UsersOp.Retrieve usersOpRetrieve,
                              LdapTemplate ldapTemplate, Token tokenClass,
                              ExpirePassword expirePassword, UserRepo.Supplementary supplementary) {
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
        this.usersOpRetrieve = usersOpRetrieve;
        this.ldapTemplate = ldapTemplate;
        this.tokenClass = tokenClass;
        this.expirePassword =expirePassword;
        this.supplementary = supplementary;
    }

    @Override
    public HttpStatus change(String uId, String newPassword, String token) {
        return new Change(usersOpRetrieve,ldapTemplate,mongoTemplate,supplementary,BASE_DN,BASE_URL, uniformLogger)
                .change(uId,newPassword,token);
    }

    @Override
    public HttpStatus reset(String userId, String oldPass, String token, int pwdin) {
        return new Reset(usersOpRetrieve,supplementary,uniformLogger,mongoTemplate,ldapTemplate,tokenClass,BASE_URL,BASE_DN).resetPassword(userId,oldPass,token,pwdin);
    }

    @Override
    public List<String> expire(String doer, JSONObject jsonObject) {
        List<UsersExtraInfo> users = new LinkedList<>();

        if (((List<String>) jsonObject.get("names")).size() == 0) {
            users.addAll(mongoTemplate.find(new Query(), UsersExtraInfo.class, Variables.col_usersExtraInfo));

        } else {
            final ArrayList<String> jsonArray = (ArrayList<String>) jsonObject.get("names");
            for (String temp : jsonArray)
                users.add(mongoTemplate.findOne(new Query(Criteria.where("_id").is(temp)), UsersExtraInfo.class, Variables.col_usersExtraInfo));
        }

        return new Expire(mongoTemplate, ldapTemplate,uniformLogger).expire(doer,users);
    }

    @Override
    public HttpStatus changePublic(String userId, String currentPassword, String newPassword) {
        return new Change(usersOpRetrieve, ldapTemplate,
                mongoTemplate,supplementary,BASE_DN,BASE_URL,tokenClass,uniformLogger)
                .publicChange(userId,currentPassword,newPassword);
    }

}

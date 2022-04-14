package parsso.idman.repoImpls.users.passwordOprations;

import net.minidev.json.JSONObject;
import one.util.streamex.StreamEx;

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
import parsso.idman.models.groups.Group;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.GroupRepo;
import parsso.idman.repos.UserRepo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

@Service
public class PasswordOpRepoImpl implements UserRepo.PasswordOp {
    final MongoTemplate mongoTemplate;
    final UniformLogger uniformLogger;
    final UserRepo.UsersOp.Retrieve usersOpRetrieve;
    final LdapTemplate ldapTemplate;
    final Token tokenClass;
    final GroupRepo.Retrieve groupRepoRetrieve;
    final ExpirePassword expirePassword;
    final UserRepo.Supplementary supplementary;

    @Value("${base.url}")
    private String BASE_URL;

    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;

    @Autowired
    public PasswordOpRepoImpl(MongoTemplate mongoTemplate, UniformLogger uniformLogger, UserRepo.UsersOp.Retrieve usersOpRetrieve,
                              LdapTemplate ldapTemplate, Token tokenClass,
                              ExpirePassword expirePassword, UserRepo.Supplementary supplementary, GroupRepo.Retrieve groupRepo) {
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
        this.usersOpRetrieve = usersOpRetrieve;
        this.ldapTemplate = ldapTemplate;
        this.tokenClass = tokenClass;
        this.expirePassword =expirePassword;
        this.supplementary = supplementary;
        this.groupRepoRetrieve = groupRepo;
    }

    @Override
    public HttpStatus change(String uId, String newPassword, String token) {
        return new Change(usersOpRetrieve,ldapTemplate,mongoTemplate,supplementary,BASE_DN,BASE_URL, tokenClass, uniformLogger)
                .change(uId,newPassword,token);
    }

    @Override
    public HttpStatus reset(String userId, String newPassword, String token) {
        return new Change(usersOpRetrieve,ldapTemplate,mongoTemplate,supplementary,BASE_DN,BASE_URL, tokenClass, uniformLogger).change(userId,newPassword,token);
    }

    @Override
    public JSONObject expire(String doer, JSONObject jsonObject) {
        List<UsersExtraInfo> users = new LinkedList<>();
        LinkedList<String> notFound = new LinkedList();

        if (((List<String>) jsonObject.get("names")).size() == 0) {
            users.addAll(mongoTemplate.find(new Query(), UsersExtraInfo.class, Variables.col_usersExtraInfo));

        } else {
            final ArrayList<String> jsonArray = (ArrayList<String>) jsonObject.get("names");
            for (String temp : jsonArray){
                UsersExtraInfo user = mongoTemplate.findOne(new Query(Criteria.where("_id").is(temp)), UsersExtraInfo.class, Variables.col_usersExtraInfo);
                if (user==null)
                    notFound.add(temp);
                else
                    users.add(user);

            }
        }
        JSONObject exceptions = new JSONObject();
        exceptions.put("notFound", notFound);
        exceptions.put("superUsers",new Expire(mongoTemplate, ldapTemplate,uniformLogger,usersOpRetrieve,BASE_DN).expire(doer,users));

        return exceptions;
    }

    @Override
    public JSONObject expireGroup(String doer, JSONObject jsonObject) {
        List<Group> groups = new LinkedList<>();
        LinkedList<String> notFound = new LinkedList();
        List<UsersExtraInfo> users = new LinkedList<>();

        if (((List<String>) jsonObject.get("names")).size() == 0) {
            groups.addAll(groupRepoRetrieve.retrieve());

        } else {
            final ArrayList<String> jsonArray = (ArrayList<String>) jsonObject.get("names");
            for (String temp : jsonArray){
                Group group = groupRepoRetrieve.retrieve(true, temp);
                if (group==null)
                    notFound.add(temp);
                else
                    groups.add(group);

            }
        }
        for(Group gr: groups){
            users.addAll(usersOpRetrieve.retrieveUsersGroup(gr.getId()));
        }
        JSONObject exceptions = new JSONObject();
        exceptions.put("notFound", notFound);
        Set<String> set = new HashSet<>(users.size());
        users.removeIf(p -> !set.add((p.get_id()).toString()));
        exceptions.put("superUsers",new Expire(mongoTemplate, ldapTemplate,uniformLogger,usersOpRetrieve,BASE_DN).expire(doer,users));

        return exceptions;
    }

    @Override
    public HttpStatus changePublic(String userId, String currentPassword, String newPassword) {
        return new Change(usersOpRetrieve, ldapTemplate,
                mongoTemplate,supplementary,BASE_DN,BASE_URL,tokenClass,uniformLogger)
                .publicChange(userId,currentPassword,newPassword);
    }

}

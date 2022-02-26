package parsso.idman.repoImpls.users.usersOprations.update;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.user.BuildAttributes;
import parsso.idman.helpers.user.ExcelAnalyzer;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.models.users.User;
import parsso.idman.repoImpls.users.usersOprations.update.helper.Parameters;
import parsso.idman.repoImpls.users.usersOprations.update.helper.*;
import parsso.idman.repos.UserRepo;

import java.io.IOException;
import java.util.List;


@Service
public class UpdateUser extends Parameters implements UserRepo.UsersOp.Update {

    final protected BuildAttributes buildAttributes;
    final protected ExcelAnalyzer excelAnalyzer;

    @Autowired
    public UpdateUser(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UniformLogger uniformLogger,
                      UserRepo.UsersOp.Retrieve userOpRetrieve, BuildAttributes buildAttributes, ExcelAnalyzer excelAnalyzer) {
        super(ldapTemplate, mongoTemplate, uniformLogger, userOpRetrieve);
        this.buildAttributes = buildAttributes;
        this.excelAnalyzer = excelAnalyzer;
    }


    public HttpStatus update(String doerID, String usid, User p) {
        return new parsso.idman.repoImpls.users.usersOprations.update.helper.UpdateUser(ldapTemplate,mongoTemplate,uniformLogger,
                userOpRetrieve,buildAttributes,excelAnalyzer)
                .update(doerID,usid,p);
    }


    @Override
    public HttpStatus groupOfUsers(String doerID, String groupId, JSONObject gu) {
        return new GroupOfUsers(userOpRetrieve,new parsso.idman.repoImpls.users.usersOprations.update.helper.UpdateUser(ldapTemplate,mongoTemplate,uniformLogger,userOpRetrieve,
                buildAttributes,excelAnalyzer)).massUsersGroupUpdate(doerID,groupId,gu);
    }

    @Override
    public JSONObject mass(String doerID, List<User> users) {
        return new parsso.idman.repoImpls.users.usersOprations.update.helper.UpdateUser(ldapTemplate,mongoTemplate,uniformLogger,userOpRetrieve,
                buildAttributes,excelAnalyzer).mass(doerID,users);
    }

    @Override
    public void usersWithSpecificOU(String doerID, String old_ou, String new_ou) {
        new UsersWithSpecificOU(uniformLogger,ldapTemplate,mongoTemplate,buildAttributes,
                new UserAttributeMapper(mongoTemplate),BASE_DN).updateUsersWithSpecificOU(doerID,old_ou,new_ou);
    }

    @Override
    public List<String> addGroupToUsers(String doer, MultipartFile file, String ou) throws IOException {
        return new GroupUser(excelAnalyzer).addGroupToUsers(doer,file,ou);
    }
}

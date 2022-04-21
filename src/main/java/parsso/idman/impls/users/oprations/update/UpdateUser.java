package parsso.idman.impls.users.oprations.update;

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
import parsso.idman.impls.users.oprations.update.helper.*;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;

import java.io.IOException;
import java.util.List;

@Service
public class UpdateUser extends Parameters implements UserRepo.UsersOp.Update {

  protected final BuildAttributes buildAttributes;

  @Autowired
  public UpdateUser(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UniformLogger uniformLogger,
      UserRepo.UsersOp.Retrieve userOpRetrieve, BuildAttributes buildAttributes) {
    super(ldapTemplate, mongoTemplate, uniformLogger, userOpRetrieve);
    this.buildAttributes = buildAttributes;
  }

  public HttpStatus update(String doerID, String usid, User p) {
    return new parsso.idman.impls.users.oprations.update.helper.UpdateUser(ldapTemplate, mongoTemplate,
        uniformLogger,
        userOpRetrieve, buildAttributes, new ExcelAnalyzer(ldapTemplate, mongoTemplate, userOpRetrieve, this),
        BASE_DN)
        .update(doerID, usid, p);
  }

  @Override
  public JSONObject groupOfUsers(String doerID, String groupId, JSONObject gu) {
    return new GroupOfUsers(userOpRetrieve,
        new parsso.idman.impls.users.oprations.update.helper.UpdateUser(ldapTemplate, mongoTemplate,
            uniformLogger, userOpRetrieve,
            buildAttributes, new ExcelAnalyzer(ldapTemplate, mongoTemplate, userOpRetrieve, this), BASE_DN))
        .massUsersGroupUpdate(doerID, groupId, gu);
  }

  @Override
  public JSONObject mass(String doerID, List<User> users) {
    return new parsso.idman.impls.users.oprations.update.helper.UpdateUser(ldapTemplate, mongoTemplate,
        uniformLogger, userOpRetrieve,
        buildAttributes, new ExcelAnalyzer(ldapTemplate, mongoTemplate, userOpRetrieve, this), BASE_DN)
        .massUpdate(doerID, users);
  }

  @Override
  public void usersWithSpecificOU(String doerID, String old_ou, String new_ou) {
    new UsersWithSpecificOU(uniformLogger, ldapTemplate, mongoTemplate, buildAttributes,
        new UserAttributeMapper(mongoTemplate), BASE_DN).updateUsersWithSpecificOU(doerID, old_ou, new_ou);
  }

  @Override
  public List<String> addGroupToUsers(String doer, MultipartFile file, String ou) throws IOException {
    return new GroupUser(new ExcelAnalyzer(ldapTemplate, mongoTemplate, userOpRetrieve, this), BASE_DN)
        .addGroupToUsers(doer, file, ou);
  }
}

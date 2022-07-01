package parsso.idman.impls.users.oprations.update;

import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.onetime.RunOneTime;
import parsso.idman.helpers.user.BuildAttributes;
import parsso.idman.helpers.user.ExcelAnalyzer;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.impls.Parameters;
import parsso.idman.impls.services.RetrieveService;
import parsso.idman.impls.settings.helper.PreferenceSettings;
import parsso.idman.impls.users.oprations.update.helper.*;
import parsso.idman.models.users.User;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;
import parsso.idman.repos.users.oprations.sub.UsersUpdateRepo;

@Service
public class UpdateUser extends Parameters implements UsersUpdateRepo {

  protected final BuildAttributes buildAttributes;
  private final RetrieveService retrieveService;

  @Autowired
  public UpdateUser(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UniformLogger uniformLogger,
      UsersRetrieveRepo userOpRetrieve, BuildAttributes buildAttributes, RetrieveService retrieveService) {
    super(ldapTemplate, mongoTemplate, uniformLogger, userOpRetrieve);
    this.buildAttributes = buildAttributes;
    this.retrieveService = retrieveService;
  }

  public HttpStatus update(String doerID, String usid, User p) {
    return new parsso.idman.impls.users.oprations.update.helper.UpdateUser(ldapTemplate, mongoTemplate,
        uniformLogger,
        userOpRetrieve, buildAttributes, new ExcelAnalyzer(userOpRetrieve, this))
        .update(doerID, usid, p);
  }

  @Override
  public JSONObject groupOfUsers(String doerID, String groupId, JSONObject gu) {
    return new GroupOfUsers(userOpRetrieve,
        new parsso.idman.impls.users.oprations.update.helper.UpdateUser(ldapTemplate, mongoTemplate,
            uniformLogger, userOpRetrieve,
            buildAttributes, new ExcelAnalyzer(userOpRetrieve, this)))
        .massUsersGroupUpdate(doerID, groupId, gu);
  }

  @Override
  public JSONObject mass(String doerID, List<User> users) {
    return new parsso.idman.impls.users.oprations.update.helper.UpdateUser(ldapTemplate, mongoTemplate,
        uniformLogger, userOpRetrieve,
        buildAttributes, new ExcelAnalyzer(userOpRetrieve, this))
        .massUpdate(doerID, users);
  }

  @Override
  public void usersWithSpecificOU(String doerID, String old_ou, String new_ou) {
    new UsersWithSpecificOU(uniformLogger, ldapTemplate, mongoTemplate, buildAttributes,
        new UserAttributeMapper(mongoTemplate)).updateUsersWithSpecificOU(doerID, old_ou, new_ou);
  }

  @Override
  public List<String> addGroupToUsers(String doer, MultipartFile file, String ou) throws IOException {
    return new GroupUser(new ExcelAnalyzer(userOpRetrieve, this))
        .addGroupToUsers(doer, file, ou);
  }

  @PostConstruct
  public void postConstruct() throws InterruptedException {
    new RunOneTime(ldapTemplate, mongoTemplate, userOpRetrieve, uniformLogger, this,
        new UserAttributeMapper(mongoTemplate), retrieveService).postConstruct();
    new PreferenceSettings(mongoTemplate).run();
  }

}

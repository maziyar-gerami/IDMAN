package parsso.idman.helpers.onetime;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;

import lombok.val;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.impls.services.RetrieveService;
import parsso.idman.impls.settings.helper.PreferenceSettings;
import parsso.idman.models.other.OneTime;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;
import parsso.idman.repos.users.oprations.sub.UsersUpdateRepo;

public class RunOneTime {
  final UniformLogger uniformLogger;
  final LdapTemplate ldapTemplate;
  final MongoTemplate mongoTemplate;
  final UsersRetrieveRepo usersOpRetrieve;
  final UsersUpdateRepo usersOpUpdate;
  final RetrieveService retrieveService;

  public RunOneTime(LdapTemplate ldapTemplate,
      MongoTemplate mongoTemplate, UsersRetrieveRepo usersOpRetrieve,
      UniformLogger uniformLogger, UsersUpdateRepo usersOpUpdate,
      UserAttributeMapper userAttributeMapper, RetrieveService retrieveService) {
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
    this.usersOpRetrieve = usersOpRetrieve;
    this.usersOpUpdate = usersOpUpdate;
    this.retrieveService = retrieveService;
  }

  public void postConstruct() throws InterruptedException {
    
    //new PreferenceSettings(mongoTemplate).run();
    try {
      if (!(mongoTemplate.findOne(
          new Query(Criteria.where("_id").is(Variables.USERID_TO_ID)), OneTime.class,
          Variables.col_OneTime).isRun()))
        new UserID(mongoTemplate).run();
    } catch (NullPointerException e) {
      new UserID(mongoTemplate).run();
    }

    try {
      if (!(mongoTemplate.findOne(
          new Query(Criteria.where("_id").is(Variables.SA_TO_SU)), OneTime.class,
          Variables.col_OneTime).isRun()))
        new SAtoSU(mongoTemplate, uniformLogger).run();
    } catch (NullPointerException e) {
      new SAtoSU(mongoTemplate, uniformLogger).run();
    }

    try {
      if (!(mongoTemplate.findOne(
          new Query(Criteria.where("_id").is(Variables.SIMPLESERVICE_FIX)), OneTime.class,
          Variables.col_OneTime).isRun()))
        new SimpleServiceFix(mongoTemplate, retrieveService).run();
    } catch (NullPointerException e) {
      new SimpleServiceFix(mongoTemplate, retrieveService).run();
    }
    try {
      if (!(mongoTemplate.findOne(
          new Query(Criteria.where("_id").is(Variables.PWD_RESET)), OneTime.class,
          Variables.col_OneTime).isRun()))
        new PWDreset(ldapTemplate, mongoTemplate, uniformLogger).run();
    } catch (NullPointerException e) {
      new PWDreset(ldapTemplate, mongoTemplate, uniformLogger).run();
    }
    try {
      if (!(mongoTemplate.findOne(
          new Query(Criteria.where("_id").is(Variables.DUPLICATE_USER)), OneTime.class,
          Variables.col_OneTime).isRun()))
        new RemoveDuplicateExtraInfo(mongoTemplate).run();
    } catch (NullPointerException e) {
      new RemoveDuplicateExtraInfo(mongoTemplate).run();
    }

    try {
      if (!(mongoTemplate.findOne(
          new Query(Criteria.where("_id").is(Variables.DISPLAY_NAME_CORRECTION)), OneTime.class,
          Variables.col_OneTime).isRun()))
        new DisplayName(mongoTemplate, usersOpRetrieve, usersOpUpdate).run();
    } catch (NullPointerException e) {
      new DisplayName(mongoTemplate, usersOpRetrieve, usersOpUpdate).run();
    }

    try {
      if (!(mongoTemplate.findOne(
          new Query(Criteria.where("_id").is(Variables.MOBILE_TO_MONGO)), OneTime.class,
          Variables.col_OneTime).isRun()))
        new MongoUserDocumentFix(mongoTemplate, usersOpRetrieve, ldapTemplate,
            new UserAttributeMapper(mongoTemplate)).run();
    } catch (NullPointerException e) {
      new MongoUserDocumentFix(mongoTemplate, usersOpRetrieve, ldapTemplate,
          new UserAttributeMapper(mongoTemplate)).run();
    }

    try {
      if (!(mongoTemplate.findOne(
          new Query(Criteria.where("_id").is(Variables.ROLE_CORRECTION)), OneTime.class,
          Variables.col_OneTime).isRun()))
        new RoleFix(mongoTemplate).run();
    } catch (NullPointerException e) {
      new RoleFix(mongoTemplate).run();
    }

    try {
      if (!(mongoTemplate.findOne(
          new Query(Criteria.where("_id").is(Variables.PWD_LOCKOUT)), OneTime.class,
          Variables.col_OneTime).isRun()))
        new PWDlockout(ldapTemplate, mongoTemplate, uniformLogger).run();
    } catch (NullPointerException e) {
      new PWDlockout(ldapTemplate, mongoTemplate, uniformLogger).run();
    }
    System.out.println(Variables.PARSSO_IDMAN);
  }
}

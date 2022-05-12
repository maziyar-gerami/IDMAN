package parsso.idman.helpers.onetime;

import lombok.val;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.models.other.OneTime;
import parsso.idman.repos.UserRepo;

public class RunOneTime {
  final UniformLogger uniformLogger;
  final LdapTemplate ldapTemplate;
  final MongoTemplate mongoTemplate;
  final UserRepo.UsersOp.Retrieve usersOpRetrieve;
  final UserRepo.UsersOp.Update usersOpUpdate;
  final String basedn;

  public RunOneTime(LdapTemplate ldapTemplate, 
      MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve usersOpRetrieve,
      UniformLogger uniformLogger, UserRepo.UsersOp.Update usersOpUpdate, String basedn,
      UserAttributeMapper userAttributeMapper) {
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
    this.usersOpRetrieve = usersOpRetrieve;
    this.usersOpUpdate = usersOpUpdate;
    this.basedn = basedn;
  }

  public void postConstruct() throws InterruptedException {

    val surun = new Runnable() {

      @Override
      public void run() {
        new SAtoSU(mongoTemplate, uniformLogger).run();
      }
    };

    val loggeInUses = new Runnable() {

      @Override
      public void run() {
        new PWDreset(ldapTemplate, mongoTemplate, uniformLogger, basedn).run();
      }
    };

    val duplicatedUsers = new Runnable() {

      @Override
      public void run() {
        new RemoveDuplicateExtraInfo(mongoTemplate).run();
      }
    };

    val displayNameFix = new Runnable() {

      @Override
      public void run() {
        new DisplayName(mongoTemplate, usersOpRetrieve, usersOpUpdate).run();
      }
    };

    val addMobileToMongo = new Runnable() {

      @Override
      public void run() {
        new MongoUserDocumentFix(mongoTemplate, usersOpRetrieve, ldapTemplate, basedn,
            new UserAttributeMapper(mongoTemplate)).run();
      }
    };

    val reoleFix = new Runnable() {

      @Override
      public void run() {
        new RoleFix(mongoTemplate).run();
      }
    };

    val addLockout = new Runnable() {

      @Override
      public void run() {
        new PWDlockout(ldapTemplate, mongoTemplate, uniformLogger,
                basedn).run();
      }
    };

    Thread sathread = new Thread(surun);
    Thread logeInUsers = new Thread(loggeInUses);
    Thread duplicated = new Thread(duplicatedUsers);
    Thread addMobile = new Thread(addMobileToMongo);
    Thread addLockou = new Thread(addLockout);
    Thread displayName = new Thread(displayNameFix);
    Thread roleFixt = new Thread(reoleFix);
    logeInUsers.start();

    OneTime b1 = mongoTemplate.findOne(
        new Query(Criteria.where("_id").is(Variables.SA_TO_SU)), OneTime.class,
        Variables.col_OneTime);
    if (b1 == null || !b1.isRun()) {
      sathread.start();
    }

    OneTime b2 = mongoTemplate.findOne(
        new Query(Criteria.where("_id").is(Variables.DUPLICATE_USER)), OneTime.class,
        Variables.col_OneTime);
    if (b2 == null || !b2.isRun()) {
      duplicated.start();
    }

    OneTime b4 = mongoTemplate.findOne(
        new Query(Criteria.where("_id").is(Variables.MOBILE_TO_MONGO)),
        OneTime.class, Variables.col_OneTime);
    if (b4 == null || !b4.isRun()) {
      addMobile.start();
    }

    OneTime b5 = mongoTemplate.findOne(
        new Query(Criteria.where("_id").is(Variables.DISPLAY_NAME_CORRECTION)),
        OneTime.class, Variables.col_OneTime);
    if (b5 == null || !b5.isRun()) {
      displayName.start();
    }

    OneTime b6 = mongoTemplate.findOne(
        new Query(Criteria.where("_id").is(Variables.PWD_LOCKOUT)),
        OneTime.class, Variables.col_OneTime);
    if (b6 == null || !b6.isRun()) {
      addLockou.start();
    }

    OneTime b7 = mongoTemplate.findOne(
        new Query(Criteria.where("_id").is(Variables.ROLE_CORRECTION)),
        OneTime.class, Variables.col_OneTime);
    if (b7 == null || !b7.isRun()) {
      roleFixt.start();
    }

    System.out.println(Variables.PARSSO_IDMAN);

  }

}

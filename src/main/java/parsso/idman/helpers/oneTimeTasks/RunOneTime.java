package parsso.idman.helpers.oneTimeTasks;

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
    final String BASE_DN;

    public RunOneTime(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve usersOpRetrieve, UniformLogger uniformLogger, UserRepo.UsersOp.Update usersOpUpdate, String BASE_DN, UserAttributeMapper userAttributeMapper) {
        this.ldapTemplate = ldapTemplate;
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
        this.usersOpRetrieve = usersOpRetrieve;
        this.usersOpUpdate = usersOpUpdate;
        this.BASE_DN = BASE_DN;
    }


    public void postConstruct() throws InterruptedException {

        val SUrunnable = new Runnable() {

            @Override
            public void run() {
                new SAtoSU(mongoTemplate, uniformLogger).run();
            }
        };


        val loggeInUses = new Runnable() {

            @Override
            public void run() {
                new PWDreset(ldapTemplate, mongoTemplate, uniformLogger, BASE_DN).run();
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
                new MongoUserDocumentFix(mongoTemplate, usersOpRetrieve,ldapTemplate,BASE_DN,new UserAttributeMapper(mongoTemplate)).run();
            }
        };


        Thread sathread = new Thread(SUrunnable);
        Thread logeInUsers = new Thread(loggeInUses);
        Thread duplicated = new Thread(duplicatedUsers);
        Thread addMobile = new Thread(addMobileToMongo);
        Thread displayName = new Thread(displayNameFix);
        logeInUsers.start();

        OneTime  b1 = mongoTemplate.findOne(new Query(Criteria.where("_id").is(Variables.SA_TO_SU)), OneTime.class, Variables.col_OneTime);
        if (b1==null || !b1.isRun()){
            sathread.start();
        }

        OneTime b2 = mongoTemplate.findOne(new Query(Criteria.where("_id").is(Variables.DUPLICATE_USER)),OneTime.class,Variables.col_OneTime);
        if(b2==null || !b2.isRun()) {
            duplicated.start();
        }

        OneTime b4 = mongoTemplate.findOne(new Query(Criteria.where("_id").is(Variables.MOBILE_TO_MONGO)),OneTime.class,Variables.col_OneTime);
        if(b4==null || !b4.isRun()) {
            addMobile.start();
        }

        OneTime b5 = mongoTemplate.findOne(new Query(Criteria.where("_id").is(Variables.DISPLAY_NAME_CORRECTION)),OneTime.class,Variables.col_OneTime);
        if(b5==null || !b5.isRun())
            displayName.start();

        System.out.println(Variables.PARSSO_IDMAN);

    }

}

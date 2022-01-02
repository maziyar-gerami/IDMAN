package parsso.idman.helpers.oneTimeTasks;

import lombok.val;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.repos.UserRepo;

public class RunOneTime {
    final UniformLogger uniformLogger;
    final LdapTemplate ldapTemplate;
    final MongoTemplate mongoTemplate;
    final UserRepo userRepo;
    final String BASE_DN;

    public RunOneTime(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,UserRepo userRepo, UniformLogger uniformLogger, String BASE_DN) {
        this.ldapTemplate = ldapTemplate;
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
        this.userRepo = userRepo;
        this.BASE_DN = BASE_DN;
    }


    public void postConstruct() {

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
                new DisplayName(mongoTemplate,userRepo).run();
            }
        };

        val userIdFix = new Runnable() {

            @Override
            public void run() {
                //new UserID(mongoTemplate).run();
            }
        };

        Thread sathread = new Thread(SUrunnable);
        Thread logeInUsers = new Thread(loggeInUses);
        Thread duplicated = new Thread(duplicatedUsers);
        Thread nameFix = new Thread(displayNameFix);
        Thread userId = new Thread(userIdFix);
        logeInUsers.start();
        sathread.start();
        duplicated.start();
        nameFix.start();
        userId.start();

        System.out.println(Variables.PARSSO_IDMAN);


    }

}

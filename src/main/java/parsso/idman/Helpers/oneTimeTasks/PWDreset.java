package parsso.idman.helpers.oneTimeTasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.BuildDnUser;
import parsso.idman.helpers.user.SimpleUserAttributeMapper;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.OneTime;
import parsso.idman.models.users.UserLoggedIn;
import parsso.idman.models.users.UsersExtraInfo;

import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import java.util.Date;
import java.util.List;

public class PWDreset {
    MongoTemplate mongoTemplate;
    LdapTemplate ldapTemplate;
    String BASE_DN;
    UniformLogger uniformLogger;
    public PWDreset(LdapTemplate ldapTemplate,
                    MongoTemplate mongoTemplate, UniformLogger uniformLogger, String BASE_DN) {
        this.ldapTemplate = ldapTemplate;
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
        this.BASE_DN = BASE_DN;
    }

    public void run() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        String[] array = {"uid","pwdHistory"};
        searchControls.setReturningAttributes(array);

        OneTime oneTime;
        Query query = new Query(Criteria.where("_id").is("PWDreset"));
        try {
            oneTime = mongoTemplate.findOne(query, OneTime.class,Variables.col_OneTime);
        }catch (NullPointerException e){
            oneTime = new OneTime("PWDreset",false, 0L);
        }

        if (oneTime== null)
            oneTime= new OneTime("PWDreset");
        if(oneTime.isRun())
            return;

        EqualsFilter equalsFilter = new EqualsFilter("objectclass","person");

        List<UserLoggedIn> usersLoggedIn = ldapTemplate.search("ou=People," + BASE_DN, equalsFilter.encode(), searchControls, new SimpleUserAttributeMapper.LoggedInUserAttributeMapper());

        int c=0;
        char[] animationChars = new char[]{'|', '/', '-', '\\'};
        for (UserLoggedIn userLoggedIn:usersLoggedIn ) {
            UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userLoggedIn.getUserId())),UsersExtraInfo.class, Variables.col_usersExtraInfo);
            try {
                assert usersExtraInfo != null;
                usersExtraInfo.setLoggedIn(userLoggedIn.isLoggedIn());
            }catch (NullPointerException e){
                continue;
            }
            try {
                mongoTemplate.save(usersExtraInfo,Variables.col_usersExtraInfo);
                ModificationItem[] modificationItems;
                modificationItems = new ModificationItem[1];
                if (usersExtraInfo.isLoggedIn())
                    modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("pwdReset","FALSE"));
                else
                    modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("pwdReset","TRUE"));

                try {
                    ldapTemplate.modifyAttributes(new BuildDnUser().buildDn(userLoggedIn.getUserId(),BASE_DN), modificationItems);
                }catch (Exception ignore){

                }

            }catch (Exception e){
                uniformLogger.info("System", new ReportMessage(Variables.MODEL_USER, userLoggedIn.getUserId(), Variables.ATTR_LOGGEDIN, Variables.ACTION_SET, Variables.RESULT_FAILED, "Writing to DB"));
            }
            int i =(++c*100/usersLoggedIn.size());

            System.out.print("Processing: " + i + "% " + animationChars[i % 4] + "\r");

        }


        OneTime oneTime1 = new OneTime("PWDreset",true,new Date().getTime());
        mongoTemplate.save(oneTime1,Variables.col_OneTime);

        System.out.println("Processing: Done!");
    }
}

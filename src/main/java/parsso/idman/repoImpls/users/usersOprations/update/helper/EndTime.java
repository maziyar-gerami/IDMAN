package parsso.idman.repoImpls.users.usersOprations.update.helper;

import org.springframework.ldap.core.LdapTemplate;
import parsso.idman.helpers.user.BuildDnUser;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;

import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

public class EndTime {

    final UserRepo.UsersOp.Retrieve userOpRetrieve;
    final LdapTemplate ldapTemplate;
    final String BASE_DN;

    public EndTime(UserRepo.UsersOp.Retrieve userOpRetrieve, LdapTemplate ldapTemplate, String BASE_DN) {
        this.userOpRetrieve = userOpRetrieve;
        this.ldapTemplate = ldapTemplate;
        this.BASE_DN = BASE_DN;
    }

    public void remove(String userID){
         Name dn = new BuildDnUser(BASE_DN).buildDn(userID);

         ModificationItem[] modificationItems;
         modificationItems = new ModificationItem[1];

         User user = userOpRetrieve.retrieveUsers(userID);

         if (user.getExpiredTime() != null) {
             modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdEndTime"));

             try {
                 ldapTemplate.modifyAttributes(dn, modificationItems);

             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
     }

}

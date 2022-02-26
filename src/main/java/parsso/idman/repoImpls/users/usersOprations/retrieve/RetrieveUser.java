package parsso.idman.repoImpls.users.usersOprations.retrieve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repoImpls.services.RetrieveService;
import parsso.idman.repoImpls.users.usersOprations.retrieve.subClass.Conditional;
import parsso.idman.repoImpls.users.usersOprations.retrieve.subClass.FullAttributes;
import parsso.idman.repoImpls.users.usersOprations.retrieve.subClass.MainAttributes;
import parsso.idman.repoImpls.users.usersOprations.retrieve.helper.Parameters;
import parsso.idman.repos.ServiceRepo;
import parsso.idman.repos.UserRepo;
import java.util.List;

@Service
public class RetrieveUser extends Parameters implements UserRepo.UsersOp.Retrieve {


    @Autowired
    public RetrieveUser(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate) {
        super(ldapTemplate, mongoTemplate);
    }

    @Override
    public List<UsersExtraInfo> mainAttributes(int page, int number) {
        return new MainAttributes(mongoTemplate,ldapTemplate,BASE_DN).get(page, number);
    }

    @Override
    public List<User> fullAttributes() {
        return new FullAttributes(ldapTemplate,mongoTemplate,BASE_DN).get();
    }


    @Override
    public User retrieveUsers(String userId) {
        return new FullAttributes(ldapTemplate,mongoTemplate,BASE_DN).get(userId);
    }

    @Override
    public User retrieveUsersWithLicensed(String userId) {
        return new Conditional(this, mongoTemplate,new RetrieveService(mongoTemplate)).licensed(userId);
    }

    @Override
    public UsersExtraInfo retrieveUserMain(String userId) {
        return new MainAttributes(mongoTemplate,ldapTemplate,BASE_DN).get(userId);
    }

    @Override
    public List<UsersExtraInfo> retrieveUsersGroup(String groupId) {
        return new Conditional(ldapTemplate,BASE_DN).group(groupId);
    }

    @Override
    public User.ListUsers retrieveUsersMainWithGroupId(String groupId, int page, int nRec) {
        return new Conditional().group(groupId,page,nRec);
    }

    @Override
    public User.ListUsers mainAttributes(int page, int number, String sortType, String groupFilter, String searchUid, String searchDisplayName, String mobile, String userStatus) {
        return new MainAttributes(mongoTemplate,ldapTemplate,BASE_DN).get(page, number, sortType, groupFilter, searchUid, searchDisplayName, mobile, userStatus);
    }
}

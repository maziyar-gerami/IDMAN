package parsso.idman.Configs;

import com.mongodb.client.MongoClients;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import parsso.idman.Models.UsersExtraInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CasUserDetailService implements AuthenticationUserDetailsService {
    @Autowired
    MongoTemplate mongoTemplate;

    String adminId;
    public CasUserDetailService(String Id){

        adminId = Id;
    }

    @Override
    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
        CasAssertionAuthenticationToken casAssertionAuthenticationToken = (CasAssertionAuthenticationToken) token;
        AttributePrincipal principal = casAssertionAuthenticationToken.getAssertion().getPrincipal();
        Collection<SimpleGrantedAuthority> collection = new ArrayList<SimpleGrantedAuthority>();
        Map attributes = principal.getAttributes();
        Query query = new Query(Criteria.where("userId").is(principal.getName()));
        String collection1 = "IDMAN_UsersExtraInfo";
        System.out.println("********************************");

        String mongoURI = "mongodb://" + "parssouser:APA00918" + "@" + "parsso2.razi.ac.ir:27017" + "/" + "parssodb";
        MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(mongoURI), "parssodb");

        UsersExtraInfo usersExtraInfo =  mongoTemplate.findOne(query, UsersExtraInfo.class, collection1);

        System.out.println("********************************");


        System.out.println(usersExtraInfo.getUserId());
        String role = null;
        if (principal.getName().equals("su"))
            collection.add(new SimpleGrantedAuthority("ROLE_" + "SUPERADMIN"));

        if (usersExtraInfo.getRole()!=null)
            collection.add(new SimpleGrantedAuthority("ROLE_" + usersExtraInfo.getRole()));

        else
            collection.add(new SimpleGrantedAuthority("ROLE_" + "USER"));

            return new User(principal.getName(), "", collection);

    }

}

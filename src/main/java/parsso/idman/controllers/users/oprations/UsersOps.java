package parsso.idman.controllers.users.oprations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.RestController;

import parsso.idman.helpers.communicate.Token;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

@RestController
public class UsersOps {
  @Value("${spring.ldap.base.dn}")
  protected String basedn;

  protected final Token tokenClass;
  protected final LdapTemplate ldapTemplate;
  protected final MongoTemplate mongoTemplate;
  protected final UsersRetrieveRepo usersOpRetrieve;

  @Autowired
  public UsersOps(Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,
      UsersRetrieveRepo usersOpRetrieve) {
    this.tokenClass = tokenClass;
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
    this.usersOpRetrieve = usersOpRetrieve;
  }
}

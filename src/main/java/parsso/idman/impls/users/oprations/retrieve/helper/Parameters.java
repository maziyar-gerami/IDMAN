package parsso.idman.impls.users.oprations.retrieve.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;

public class Parameters {
  final protected LdapTemplate ldapTemplate;
  final protected MongoTemplate mongoTemplate;
  @Value("${spring.ldap.base.dn}")
  protected String BASE_DN;

  public Parameters(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate) {
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
  }

}

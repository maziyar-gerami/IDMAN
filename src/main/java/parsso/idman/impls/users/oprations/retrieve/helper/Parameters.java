package parsso.idman.impls.users.oprations.retrieve.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;

import parsso.idman.repos.ServiceRepo;

public class Parameters {
  final protected LdapTemplate ldapTemplate;
  final protected MongoTemplate mongoTemplate;
  final protected ServiceRepo ServiceRepo;
  @Value("${spring.ldap.base.dn}")
  protected String BASE_DN;

  public Parameters(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, ServiceRepo serviceRepo) {
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
    this.ServiceRepo = serviceRepo;
  }

}

package parsso.idman.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;

import parsso.idman.helpers.UniformLogger;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

public class Parameters {
  protected LdapTemplate ldapTemplate;
  protected MongoTemplate mongoTemplate;
  protected UniformLogger uniformLogger;
  protected UsersRetrieveRepo userOpRetrieve;

  @Autowired
  public Parameters(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UniformLogger uniformLogger,
      UsersRetrieveRepo userOpRetrieve) {
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
    this.userOpRetrieve = userOpRetrieve;
  }

  @Autowired
  public Parameters(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  @Autowired
  public Parameters(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate) {
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
  }

}

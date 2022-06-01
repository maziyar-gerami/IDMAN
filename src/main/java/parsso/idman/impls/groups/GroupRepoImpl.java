package parsso.idman.impls.groups;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONObject;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.impls.Parameters;
import parsso.idman.impls.groups.sub.CreateGroup;
import parsso.idman.impls.groups.sub.DeleteGroup;
import parsso.idman.impls.groups.sub.RetrieveGroup;
import parsso.idman.impls.groups.sub.UpdateGroup;
import parsso.idman.models.groups.Group;
import parsso.idman.models.users.User;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.GroupRepo;

@Service
public class GroupRepoImpl extends Parameters implements GroupRepo  {
  private FilesStorageService filesStorageService;


  @Autowired
  public GroupRepoImpl(LdapTemplate ldapTemplate,MongoTemplate mongoTemplate, UniformLogger uniformLogger, FilesStorageService filesStorageService){
    super(ldapTemplate, mongoTemplate,uniformLogger);
  }

  @Override
  public HttpStatus create(String doerId, Group ou) {
    return new CreateGroup(ldapTemplate,mongoTemplate, uniformLogger).create(doerId, ou);
  }

  @Override
  public List<Group> retrieve() {
    return new RetrieveGroup(ldapTemplate, mongoTemplate).retrieve();
  }

  @Override
  public Group retrieve(boolean simple, String name) {
    return new RetrieveGroup(ldapTemplate,mongoTemplate).retrieve(simple, name);
  }

  @Override
  public List<Group> retrieve(User user) {
    return new RetrieveGroup(ldapTemplate, mongoTemplate).retrieve(user);
  }

  @Override
  public HttpStatus update(String doerID, String name, Group ou) {
    return new UpdateGroup(ldapTemplate, mongoTemplate, uniformLogger, filesStorageService).update(doerID, name, ou);
  }

  @Override
  public HttpStatus remove(String doerId, JSONObject jsonObject) {
    return new DeleteGroup(ldapTemplate, mongoTemplate, uniformLogger).remove(doerId, jsonObject);
  }
  
}

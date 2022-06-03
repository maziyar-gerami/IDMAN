package parsso.idman.impls.users.profilepic;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import parsso.idman.impls.users.profilepic.sub.Delete;
import parsso.idman.impls.users.profilepic.sub.Retrieve;
import parsso.idman.impls.users.profilepic.sub.Upload;
import parsso.idman.models.users.User;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.users.ProfilePicRepo;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;
import parsso.idman.repos.users.oprations.sub.UsersUpdateRepo;

@Service
public class ProfilePicRepoImpl implements ProfilePicRepo {
  final MongoTemplate mongoTemplate;
  final UsersRetrieveRepo usersOpRetrieve;
  final UsersUpdateRepo usersOpUpdate;
  final FilesStorageService storageService;

  @Autowired
  public ProfilePicRepoImpl(MongoTemplate mongoTemplate, UsersRetrieveRepo usersOpRetrieve,
      UsersUpdateRepo usersOpUpdate, FilesStorageService storageService) {
    this.mongoTemplate = mongoTemplate;
    this.usersOpRetrieve = usersOpRetrieve;
    this.usersOpUpdate = usersOpUpdate;
    this.storageService = storageService;
  }

  @Override
  public String retrieve(HttpServletResponse response, User user) {
    return new Retrieve(mongoTemplate).retrieve(response, user);
  }

  @Override
  public byte[] retrieve(User user) {
    return new Retrieve(mongoTemplate).retrieve(user);
  }

  @Override
  public boolean upload(MultipartFile file, String name) {
    return new Upload(mongoTemplate, usersOpRetrieve, usersOpUpdate, storageService).upload(file, name);
  }

  @Override
  public boolean delete(User user) {
    return new Delete(mongoTemplate, usersOpRetrieve, usersOpUpdate).delete(user);
  }
}

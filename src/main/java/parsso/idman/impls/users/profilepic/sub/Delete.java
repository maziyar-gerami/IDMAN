package parsso.idman.impls.users.profilepic.sub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.User;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;
import parsso.idman.repos.users.oprations.sub.UsersUpdateRepo;

import java.io.File;

public class Delete {
  final MongoTemplate mongoTemplate;
  final UsersRetrieveRepo usersOpRetrieve;
  final UsersUpdateRepo usersOpUpdate;

  @Autowired
  public Delete(MongoTemplate mongoTemplate, UsersRetrieveRepo usersOpRetrieve,
      UsersUpdateRepo usersOpUpdate) {
    this.mongoTemplate = mongoTemplate;
    this.usersOpRetrieve = usersOpRetrieve;
    this.usersOpUpdate = usersOpUpdate;
  }

  public boolean delete(User user) {
    String uploadedFilesPath = new Settings(mongoTemplate).retrieve(Variables.PROFILE_PHOTO_PATH).getValue();
    File oldPic = new File(uploadedFilesPath + user.getPhoto());
    user.getUsersExtraInfo().setPhotoName(null);
    usersOpUpdate.update(user.get_id().toString(), user.get_id().toString(), user);
    return oldPic.delete();
  }
}

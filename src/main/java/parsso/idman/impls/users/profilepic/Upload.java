package parsso.idman.impls.users.profilepic;

import org.apache.commons.io.FilenameUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.User;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.UserRepo;

import java.io.File;

public class Upload {
  final MongoTemplate mongoTemplate;
  final UserRepo.UsersOp.Retrieve usersOpRetrieve;
  final UserRepo.UsersOp.Update usersOpUpdate;
  final FilesStorageService storageService;

  public Upload(MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve usersOpRetrieve,
      UserRepo.UsersOp.Update usersOpUpdate, FilesStorageService storageService) {
    this.mongoTemplate = mongoTemplate;
    this.usersOpRetrieve = usersOpRetrieve;
    this.storageService = storageService;
    this.usersOpUpdate = usersOpUpdate;
  }

  public boolean upload(MultipartFile file, String name) {
    String uploadedFilesPath = new Settings(mongoTemplate).retrieve(Variables.PROFILE_PHOTO_PATH).getValue();
    User userUpdate = usersOpRetrieve.retrieveUsers(name);

    String extension = FilenameUtils.getExtension(file.getOriginalFilename());

    File newPic = new File(uploadedFilesPath + name + "." + extension);
    File oldPic = new File(uploadedFilesPath + userUpdate.getPhoto());
    userUpdate.setPhoto(newPic.getName());
    if (usersOpUpdate.update(userUpdate.get_id().toString(), userUpdate.get_id().toString(), userUpdate) != null) {
      try {
        oldPic.delete();
      } catch (Exception ignored) {
      }
    }

    try {
      storageService.saveProfilePhoto(file, newPic.getName());
    } catch (Exception ignored) {
      return false;
    }

    return true;
  }

}

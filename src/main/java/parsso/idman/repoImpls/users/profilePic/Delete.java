package parsso.idman.repoImpls.users.profilePic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;

import java.io.File;

public class Delete {
    final MongoTemplate mongoTemplate;
    final UserRepo.UsersOp.Retrieve usersOpRetrieve;
    final UserRepo.UsersOp.Update usersOpUpdate;

    @Autowired
    public Delete(MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve usersOpRetrieve, UserRepo.UsersOp.Update usersOpUpdate) {
        this.mongoTemplate = mongoTemplate;
        this.usersOpRetrieve = usersOpRetrieve;
        this.usersOpUpdate = usersOpUpdate;
    }

    public boolean delete(User user){
        String uploadedFilesPath = new Settings(mongoTemplate).retrieve(Variables.PROFILE_PHOTO_PATH).getValue();
        File oldPic = new File(uploadedFilesPath + user.getPhoto());
        user.getUsersExtraInfo().setPhotoName(null);
        usersOpUpdate.update(user.get_id().toString(), user.get_id().toString(), user);
        return oldPic.delete();
    }
}


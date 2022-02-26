package parsso.idman.repoImpls.users.profilePic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.models.users.User;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.UserRepo;
import javax.servlet.http.HttpServletResponse;

@Service
public class ProfilePicRepoImpl implements UserRepo.ProfilePic {
    final MongoTemplate mongoTemplate;
    final UserRepo.UsersOp.Retrieve usersOpRetrieve;
    final UserRepo.UsersOp.Update usersOpUpdate;
    final FilesStorageService storageService;

    @Autowired
    public ProfilePicRepoImpl(MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve usersOpRetrieve,UserRepo.UsersOp.Update usersOpUpdate, FilesStorageService storageService) {
        this.mongoTemplate = mongoTemplate;
        this.usersOpRetrieve = usersOpRetrieve;
        this.usersOpUpdate = usersOpUpdate;
        this.storageService = storageService;
    }

    @Override
    public String retrieve(HttpServletResponse response, User user) {
        return new Retrieve(mongoTemplate).retrieve(response,user);
    }

    @Override
    public byte[] retrieve(User user) {
        return new Retrieve(mongoTemplate).retrieve(user);
    }

    @Override
    public boolean upload(MultipartFile file, String name) {
        return new Upload(mongoTemplate,usersOpRetrieve,usersOpUpdate,storageService).upload(file,name);
    }

    @Override
    public boolean delete(User user) {
        return new Delete(mongoTemplate, usersOpRetrieve, usersOpUpdate).delete(user);
    }
}

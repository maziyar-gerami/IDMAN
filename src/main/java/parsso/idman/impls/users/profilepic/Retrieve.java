package parsso.idman.impls.users.profilepic;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.User;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class Retrieve {
  final MongoTemplate mongoTemplate;

  public Retrieve(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public String retrieve(HttpServletResponse response, User user){
    String uploadedFilesPath = new Settings(mongoTemplate).retrieve(Variables.PROFILE_PHOTO_PATH).getValue();

    if (user.getPhoto() == null){
      return "NotExist";
    }

    File file = new File(uploadedFilesPath + user.getPhoto());

    if (file.exists()){
      try {
        String contentType = "image/png";
        response.setContentType(contentType);
        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(file);
        // copy from in to out
        IOUtils.copy(in, out);
        out.close();
        in.close();
        return "OK";
      } catch (Exception e) {
        return "Problem";

      }
    }
    return "NotExist";
  }

  public byte[] retrieve(User user) {
    String uploadedFilesPath = new Settings(mongoTemplate).retrieve(Variables.PROFILE_PHOTO_PATH).getValue();
    File file = new File(uploadedFilesPath + user.getPhoto());
    byte[] media = null;

    if (file.exists()) {
      try {
        FileInputStream out = new FileInputStream(file);
        // copy from in to out
        media = IOUtils.toByteArray(out);
        out.close();
        return media;
      } catch (Exception e) {
        return media;
      }
    }
    return null;
  }

}

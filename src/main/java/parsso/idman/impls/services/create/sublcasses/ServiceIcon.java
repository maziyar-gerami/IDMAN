package parsso.idman.impls.services.create.sublcasses;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.repos.FilesStorageService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;

public class ServiceIcon {
  MongoTemplate mongoTemplate;
  FilesStorageService storageService;
  String BASE_URL;

  public ServiceIcon(FilesStorageService storageService, String BASE_URL,MongoTemplate mongoTemplate) {
    this.storageService = storageService;
    this.mongoTemplate =mongoTemplate;
    this.BASE_URL = BASE_URL;
  }


  public String show(HttpServletResponse response, String fileName) {

    String iconPath = new Settings(mongoTemplate).retrieve(Variables.SERVICES_ICON_PATH).getValue();

    File file = new File(iconPath + fileName);
    if (file.exists()) {
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

  public String upload(MultipartFile file) {
    Date date = new Date();
    String fileName = date.getTime() + "_" + file.getOriginalFilename();

    try {
      storageService.saveIcon(file, fileName);
      return BASE_URL + "/api/public/icon/" + fileName;
      // return uploadedFilesPath+userId+timeStamp+file.getOriginalFilename();

    } catch (Exception e) {
      return null;
    }
  }
}

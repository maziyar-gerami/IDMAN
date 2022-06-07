package parsso.idman.impls.services.create.sublcasses;

import org.springframework.web.multipart.MultipartFile;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Variables;
import parsso.idman.repos.FilesStorageService;

import java.util.Date;

public class Metadata {
  final FilesStorageService storageService;
  final String BASE_URL;

  public Metadata(FilesStorageService storageService) {
    this.storageService = storageService;
    this.BASE_URL = Prefs.get(Variables.PREFS_BASE_URL);
  }

  public String upload(MultipartFile file) {
    Date date = new Date();
    String fileName = date.getTime() + "_" + file.getOriginalFilename();

    try {
      storageService.saveMetadata(file, fileName);
      return BASE_URL + "/api/public/metadata/" + fileName;
      // return uploadedFilesPath+userId+timeStamp+file.getOriginalFilename();

    } catch (Exception e) {
      return null;
    }
  }
}

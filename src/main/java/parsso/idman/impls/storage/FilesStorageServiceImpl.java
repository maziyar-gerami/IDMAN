package parsso.idman.impls.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.repos.FilesStorageService;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
  Path servicesPathRoot;
  String serviceIcon;
  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  public FilesStorageServiceImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void init() {
    /*
    String path = new Settings(mongoTemplate).retrieve(Variables.PROFILE_PHOTO_PATH).getValue();
    photoPathRoot = Paths.get(path);

    try {
      if (Files.notExists(photoPathRoot))
        Files.createDirectory(photoPathRoot);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload photo!");
    }

    path = new Settings(mongoTemplate).retrieve(Variables.SERVICE_FOLDER_PATH).getValue();
    servicesPathRoot = Paths.get(path);

    try {
      if (Files.notExists(servicesPathRoot))
        Files.createDirectory(servicesPathRoot);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for services!");
    }
    */

  }

  @Override
  public void saveMetadata(MultipartFile file, String name) {
    String path = new Settings().retrieve(Variables.METADATA_PATH).getValue();
    Path metadataPath = Paths.get(String.valueOf(path));

    try {
      Files.copy(file.getInputStream(), metadataPath.resolve(name));
    } catch (Exception e) {
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }

  @Override
  public void saveProfilePhoto(MultipartFile file, String name) {

    String photoPathRoot = new Settings(mongoTemplate).retrieve(Variables.PROFILE_PHOTO_PATH).getValue();
    Path path = Paths.get(photoPathRoot);

    try {
      System.out.println(photoPathRoot);

      Files.copy(file.getInputStream(), path.resolve(name));

    } catch (Exception e) {
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }

  @Override
  public Resource load(String filename) {
    String photoPathRoot = new Settings(mongoTemplate).retrieve(Variables.PROFILE_PHOTO_PATH).getValue();
    Path path = Paths.get(photoPathRoot);
    
    try {
      Path file = path.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void saveIcon(MultipartFile file, String fileName) {
    serviceIcon = new Settings(mongoTemplate).retrieve(Variables.SERVICES_ICON_PATH).getValue();
    try {
      Path pathServices = Paths.get(serviceIcon.toString());
      Files.copy(file.getInputStream(), pathServices.resolve(fileName));
    } catch (Exception e) {
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }
}
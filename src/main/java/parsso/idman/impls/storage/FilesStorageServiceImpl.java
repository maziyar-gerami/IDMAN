package parsso.idman.impls.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Setting;
import parsso.idman.repos.FilesStorageService;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;

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
  @PostConstruct
  public void init() throws java.io.IOException {
    Query query = new Query(Criteria.where("groupEN").is("Storage"));
    List<Setting> storageSetting = mongoTemplate.find(query, Setting.class, Variables.col_properties);
    for (Setting setting : storageSetting) {
      Path path = Paths.get(setting.getValue());
      try {
        if (Files.notExists(path))
          Files.createDirectories(path);
      } catch (IOException io) {
        throw new RuntimeException("Could not initialize folder for " + setting.getValue());
      }
    }

  }

  @Override
  public void saveMetadata(MultipartFile file, String name) {
    try {
      init();
    } catch (java.io.IOException e1) {
      e1.printStackTrace();
    }
    String path = new Settings(mongoTemplate).retrieve(Variables.METADATA_PATH).getValue();
    Path metadataPath = Paths.get(String.valueOf(path));

    try {
      Files.copy(file.getInputStream(), metadataPath.resolve(name));
    } catch (Exception e) {
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }

  @Override
  public void saveProfilePhoto(MultipartFile file, String name) {

    try {
      init();
    } catch (java.io.IOException e1) {
      e1.printStackTrace();
    }

    String photoPathRoot = new Settings(mongoTemplate).retrieve(Variables.PROFILE_PHOTO_PATH).getValue();
    Path path = Paths.get(photoPathRoot);

    try {

      Files.copy(file.getInputStream(), path.resolve(name));

    } catch (Exception e) {
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }

  @Override
  public Resource load(String filename) {

    try {
      init();
    } catch (java.io.IOException e1) {
      e1.printStackTrace();
    }

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

    try {
      init();
    } catch (java.io.IOException e1) {
      e1.printStackTrace();
    }
    
    serviceIcon = new Settings(mongoTemplate).retrieve(Variables.SERVICES_ICON_PATH).getValue();
    try {
      Path pathServices = Paths.get(serviceIcon.toString());
      Files.copy(file.getInputStream(), pathServices.resolve(fileName));
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }
}
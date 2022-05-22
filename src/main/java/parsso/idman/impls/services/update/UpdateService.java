package parsso.idman.impls.services.update;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import parsso.idman.helpers.UniformLogger;
import parsso.idman.impls.services.RetrieveService;
import parsso.idman.impls.services.create.sublcasses.Metadata;
import parsso.idman.impls.services.create.sublcasses.ServiceIcon;
import parsso.idman.impls.services.update.updateClasses.Update;
import parsso.idman.models.services.Service;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.ServiceRepo;

@org.springframework.stereotype.Service
public class UpdateService implements ServiceRepo.Update {
  FilesStorageService filesStorageService;
  @Value("${base.url}")
  private String BASE_URL;
  private UniformLogger uniformLogger;
  private RetrieveService retrieveService;
  private MongoTemplate mongoTemplate;

  @Autowired
  public UpdateService(FilesStorageService filesStorageService, UniformLogger uniformLogger,RetrieveService retrieveService, MongoTemplate mongoTemplate) {
    this.filesStorageService = filesStorageService;
    this.uniformLogger = uniformLogger;
    this.retrieveService = retrieveService;
    this.mongoTemplate = mongoTemplate;
  }

  public UpdateService(FilesStorageService storageService) {
  }

  @Override
  public String uploadMetadata(MultipartFile file) {
    return new Metadata(filesStorageService, BASE_URL).upload(file);
  }

  @Override
  public void updateOuIdChange(String doerID, Service service, long sid, String name, String oldOu, String newOu) {

  }

  @Override
  public HttpStatus updateService(String doerID, long id, JSONObject jsonObject, String system) {
    return new Update(mongoTemplate, BASE_URL, uniformLogger,retrieveService).update(doerID, id, jsonObject, system);
  }

  @Override
  public String uploadIcon(MultipartFile file) {
    return new ServiceIcon(filesStorageService, BASE_URL).upload(file);
  }
}

package parsso.idman.impls.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.service.Position;
import parsso.idman.helpers.service.Trim;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.services.serviceType.SimpleService;
import parsso.idman.repos.ServiceRepo;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DeleteService implements ServiceRepo.Delete {
  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;

  @Autowired
  public DeleteService(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  @Override
  public LinkedList<String> delete(String doerID, JSONObject jsonObject) {
    File folder = new File(new Settings(mongoTemplate).retrieve(Variables.SERVICE_FOLDER_PATH).getValue());
    List<String> allFiles = Arrays.asList(Objects.requireNonNull(folder.list()));
    List selectedFiles = new LinkedList();

    if (jsonObject.size() == 0)
      selectedFiles = allFiles;

    else {
      ArrayList<Long> files = (ArrayList<Long>) jsonObject.get("names");
      for (long file : files)
        for (Object aFile : allFiles)
          if (aFile.toString().contains((String.valueOf(file))))
            selectedFiles.add(aFile);

    }

    LinkedList<String> notDeleted = null;

    for (Object file : selectedFiles) {
      File serv = new File(
          new Settings(mongoTemplate).retrieve(Variables.SERVICE_FOLDER_PATH).getValue() + file.toString());
      if (!(serv.delete()))
        Objects.requireNonNull(notDeleted).add((String) file);

    }

    for (Object file : selectedFiles) {
      long id = Trim.extractIdFromFile(file.toString());
      Query query = new Query(Criteria.where("_id").is(id));
      SimpleService microService = mongoTemplate.findOne(query, SimpleService.class,
          Variables.col_servicesExtraInfo);
      new Position(mongoTemplate).delete(Objects.requireNonNull(microService).getPosition());
      try {
        mongoTemplate.remove(query, SimpleService.class, Variables.col_servicesExtraInfo);
        uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_SERVICE, id, "",
            Variables.ACTION_DELETE, Variables.RESULT_SUCCESS, ""));

      } catch (Exception e) {
        uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_SERVICE, id, "",
            Variables.ACTION_DELETE, Variables.RESULT_FAILED, "Writing to mongoDB"));

      }

    }

    return notDeleted;
  }
}

package parsso.idman.impls.services.update.updateClasses;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.services.Service;
import parsso.idman.repos.users.oprations.sub.UsersUpdateRepo;

import java.io.FileWriter;
import java.io.IOException;

public class GroupID {
  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;
  UsersUpdateRepo updateUsers;

  public void change(String doerID, Service service, long sid, String name, String oldOu, String newOu)
      throws IOException {

    // Update ou
    updateUsers.usersWithSpecificOU(doerID, oldOu, newOu);

    // Update text
    // String fileName = String.valueOf(sid);
    // String s1 = fileName.replaceAll("\\s+", "");
    String filePath = name + "-" + sid + ".json";

    ObjectMapper mapper = new ObjectMapper();
    // Converting the Object to JSONString
    String jsonString = mapper.writeValueAsString(service);

    try {
      FileWriter file = new FileWriter(
          new Settings(mongoTemplate).retrieve(Variables.SERVICE_FOLDER_PATH).getValue() + filePath, false);
      file.write(jsonString);
      file.close();
      uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_SERVICE, sid, "", Variables.ACTION_UPDATE,
          Variables.RESULT_SUCCESS, service, ""));
    } catch (Exception e) {
      e.printStackTrace();
      uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, sid, "", Variables.ACTION_UPDATE,
          Variables.RESULT_FAILED, service, ""));

    }

  }
}

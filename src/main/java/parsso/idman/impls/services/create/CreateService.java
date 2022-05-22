package parsso.idman.impls.services.create;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.service.Position;
import parsso.idman.impls.services.types.CAS;
import parsso.idman.impls.services.types.OAuth;
import parsso.idman.impls.services.types.SAML;
import parsso.idman.impls.services.update.UpdateService;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.services.Schedule;
import parsso.idman.models.services.servicesSubModel.ExtraInfo;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.ServiceRepo;
import parsso.idman.utils.other.GenerateUUID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CreateService implements ServiceRepo.Create {
  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;
  FilesStorageService storageService;
  @Value("${base.url}")
  private String BASE_URL;

  @Autowired
  public CreateService(MongoTemplate mongoTemplate, UniformLogger uniformLogger, FilesStorageService storageService) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
    this.storageService = storageService;
  }

  public long createService(String doerID, JSONObject jsonObject, String system) throws IOException, ParseException {

    ExtraInfo extraInfo = new ExtraInfo();
    ArrayList<LinkedHashMap> dailyAccess = null;

    try {
      LinkedHashMap jsonObject1 = (LinkedHashMap) jsonObject.get("extraInfo");
      dailyAccess = (ArrayList<LinkedHashMap>) jsonObject1.get("dailyAccess");
    } catch (Exception e) {
      e.printStackTrace();
    }
    long id = 0;
    JSONObject jsonExtraInfo = new JSONObject();

    extraInfo.setUrl(jsonExtraInfo.get("url") != null ? jsonExtraInfo.get("url").toString()
        : jsonObject.get("serviceId").toString());

    if (BASE_URL.contains("localhost"))
      extraInfo.setUUID(GenerateUUID.getUUID());

    if (system.equalsIgnoreCase("cas"))
      id = new CAS(mongoTemplate, uniformLogger).create(doerID, jsonObject);

    else if (system.equalsIgnoreCase("saml"))
      id = new SAML(mongoTemplate, uniformLogger).create(doerID, jsonObject);

    else if (system.equalsIgnoreCase("OAuth"))
      id = new OAuth(mongoTemplate, uniformLogger).create(doerID, jsonObject);

    if (dailyAccess != null) {

      extraInfo.setDailyAccess((List<Schedule>) jsonExtraInfo.get("dailyAccess"));

      LinkedHashMap jsonObjectTemp = (LinkedHashMap) jsonObject.get("accessStrategy");
      jsonObject.remove("accessStrategy");
      jsonObjectTemp.remove("@class");
      jsonObjectTemp.put("@class", "org.apereo.cas.services.RemoteEndpointServiceAccessStrategy");
      jsonObjectTemp.put("endPointUrl", BASE_URL + "/api/serviceCheck/" + id);
      jsonObjectTemp.put("acceptableResponseCodes", "200");
      jsonObject.put("accessStrategy", jsonObjectTemp);
      new UpdateService(storageService).updateService("System", id, jsonObject, system);

    }

    String jsonString = new Gson().toJson(jsonObject.get("extraInfo"), Map.class);

    JSONParser parser = new JSONParser();
    JSONObject jsonObjectExtraInfo = (JSONObject) parser.parse(jsonString);

    extraInfo = new ExtraInfo().setExtraInfo(id, extraInfo, jsonObjectExtraInfo,
        new Position(mongoTemplate).lastPosition() + 1);

    try {
      mongoTemplate.save(extraInfo, Variables.col_servicesExtraInfo);

      return id;
    } catch (Exception e) {
      e.printStackTrace();
      uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
          Variables.ACTION_CREATE, Variables.RESULT_FAILED, "Writing to mongoDB"));
      return 0;
    }

  }

}

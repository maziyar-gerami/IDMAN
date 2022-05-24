package parsso.idman.impls.services.update.updateClasses;

import org.json.simple.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.impls.services.types.CAS;
import parsso.idman.impls.services.types.OAuth;
import parsso.idman.impls.services.types.SAML;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.services.Schedule;
import parsso.idman.models.services.servicesSubModel.ExtraInfo;
import parsso.idman.repos.ServiceRepo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Update {
  final MongoTemplate mongoTemplate;
  final String BASE_URL;
  final UniformLogger uniformLogger;
  final ServiceRepo.Retrieve serviceRetrieve;

  public Update(MongoTemplate mongoTemplate, String BASE_URL, UniformLogger uniformLogger,
      ServiceRepo.Retrieve serviceRetrieve) {
    this.mongoTemplate = mongoTemplate;
    this.BASE_URL = BASE_URL;
    this.uniformLogger = uniformLogger;
    this.serviceRetrieve = serviceRetrieve;
  }


    



  public HttpStatus update(String doerID, long id, JSONObject jsonObject, String system) {
    JSONObject JsonExtraInfo = null;

    ExtraInfo extraInfo = new ExtraInfo();

    Query query = new Query(Criteria.where("_id").is(id));

    ExtraInfo oldExtraInfo = mongoTemplate.findOne(query, ExtraInfo.class, Variables.col_servicesExtraInfo);

    if (jsonObject.get("extraInfo") != null) {

      if (jsonObject.get("extraInfo").getClass().toString().equals("class org.json.simple.JSONObject"))
        JsonExtraInfo = (JSONObject) jsonObject.get("extraInfo");

      else if (jsonObject.get("extraInfo").getClass().toString().equals("class java.util.LinkedHashMap"))
        JsonExtraInfo = new JSONObject((Map) jsonObject.get("extraInfo"));

      extraInfo.setUrl(
          JsonExtraInfo != null && JsonExtraInfo.get("url") != null ? JsonExtraInfo.get("url").toString()
              : jsonObject.get("serviceId").toString());

      extraInfo.setUrl(
          Objects.requireNonNull(JsonExtraInfo).get("url") != null ? JsonExtraInfo.get("url").toString()
              : Objects.requireNonNull(oldExtraInfo).getUrl());

      extraInfo.setNotificationApiURL((String) JsonExtraInfo.get("notificationApiURL"));

      extraInfo.setNotificationApiKey((String) JsonExtraInfo.get("notificationApiKey"));

      extraInfo.setName((String) JsonExtraInfo.get("name"));

      extraInfo.setDescription((String) JsonExtraInfo.get("description"));

      extraInfo.setServiceId((String) JsonExtraInfo.get("serviceId"));

      if (JsonExtraInfo.get("dailyAccess") != null) {

        extraInfo.setDailyAccess((List<Schedule>) JsonExtraInfo.get("dailyAccess"));

        LinkedHashMap jsonObjectTemp = (LinkedHashMap) jsonObject.get("accessStrategy");
        jsonObject.remove("accessStrategy");
        jsonObjectTemp.remove("@class");
        jsonObjectTemp.put("@class", "org.apereo.cas.services.RemoteEndpointServiceAccessStrategy");
        jsonObjectTemp.put("endPointUrl", BASE_URL + "/api/serviceCheck/" + id);
        jsonObjectTemp.put("acceptableResponseCodes", "200");
        jsonObject.put("accessStrategy", jsonObjectTemp);

      }

      try {
        extraInfo.setPosition(Objects.requireNonNull(oldExtraInfo).getPosition());
      } catch (Exception ignored) {
      }

      extraInfo.setId(id);
    }

    if (system.equalsIgnoreCase("cas")) {
      try {
        mongoTemplate.save(extraInfo, Variables.col_servicesExtraInfo);

      } catch (Exception e) {
        e.printStackTrace();
        uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
            Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "writing to mongoDB"));
        return HttpStatus.FORBIDDEN;
      }
      return new CAS(mongoTemplate, uniformLogger).update(doerID, id, jsonObject);

    } else if (system.equalsIgnoreCase("saml")) {
      try {
        mongoTemplate.save(extraInfo, Variables.col_servicesExtraInfo);

      } catch (Exception e) {
        e.printStackTrace();
        uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
            Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "writing to mongoDB"));
        return HttpStatus.FORBIDDEN;
      }

      return new SAML(mongoTemplate, uniformLogger).update(doerID, id, jsonObject);
    } else if (system.equalsIgnoreCase("OAuth"))
      try {
        mongoTemplate.save(extraInfo, Variables.col_servicesExtraInfo);

      } catch (Exception e) {
        e.printStackTrace();
        uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
            Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "writing to mongoDB"));
        return HttpStatus.FORBIDDEN;
      }

    return new OAuth(mongoTemplate, uniformLogger).update(doerID, id, jsonObject);
  }
}

package parsso.idman.impls.services;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import lombok.val;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.service.Notifs;
import parsso.idman.impls.services.create.sublcasses.Analyze;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.services.Service;
import parsso.idman.models.services.serviceType.MicroService;
import parsso.idman.models.services.serviceType.SimpleService;
import parsso.idman.models.services.servicesSubModel.ExtraInfo;
import parsso.idman.models.users.User;
import parsso.idman.repos.ServiceRepo;



@org.springframework.stereotype.Service

@SuppressWarnings({ "unchecked" })
public class RetrieveService implements ServiceRepo.Retrieve {

  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;

  @Autowired
  public RetrieveService(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public RetrieveService(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;

  }

  public List<MicroService> userServices(User user) {

    List<Service> services = listServicesFull();

    LinkedList<Service> relatedList = new LinkedList<>();

    for (Service service : services) {

      if (service.getAccessStrategy() != null)
        if (service.getAccessStrategy().getRequiredAttributes() != null)
          if (service.getAccessStrategy().getRequiredAttributes().get("ou") != null) {

            Object member = service.getAccessStrategy().getRequiredAttributes().get("ou");
            if (member != null) {
              JSONArray s = (JSONArray) member;

              if (user.getMemberOf() != null)
                for (int i = 0; i < user.getMemberOf().size(); i++)
                  for (int j = 0; j < ((JSONArray) s.get(1)).size(); j++) {
                    if (user.getMemberOf().get(i).equals(((JSONArray) s.get(1)).get(j))
                        && !relatedList.contains(service)) {
                      relatedList.add(service);
                      break;

                    }
                  }
            }

          }

      try {
        if (((List<String>) (((JSONArray) (Objects.requireNonNull(service.getAccessStrategy())
            .getRequiredAttributes().get("uid"))).get(1))).contains(user.get_id()))
          relatedList.add(service);
      } catch (NullPointerException ignored) {

      }

    }
    List<MicroService> microServices = new LinkedList<>();
    MicroService microService = null;

    for (Service service : relatedList) {
      Query query = new Query(Criteria.where("_id").is(service.getId()));
      try {
        JSONObject jsonObject = mongoTemplate.findOne(query, JSONObject.class, Variables.col_servicesExtraInfo);
        microService = new MicroService((Long) jsonObject.get("_id"), jsonObject.get("url").toString(),
            Integer.parseInt(jsonObject.get("position").toString()));
      } catch (Exception e) {
        e.printStackTrace();
        microService = new MicroService(service.getId(), service.getServiceId());
        uniformLogger.error(Variables.DOER_SYSTEM,
            new ReportMessage(Variables.MODEL_SERVICE, service.getId(), "",
                Variables.ACTION_RETRIEVE, Variables.RESULT_FAILED,
                "unable read extra info from mongo"));

      } finally {
        MicroService fMicro = new MicroService(service, microService);

        try {
          fMicro.setNotification(new Notifs().getNotifications(user.get_id().toString(),
              service.getExtraInfo().getNotificationApiURL(),
              service.getExtraInfo().getNotificationApiKey()));
        } catch (Exception ignored) {
        }

        microServices.add(fMicro);

      }
    }

    Collections.sort(microServices);
    return microServices;

  }

  @Override
  public List<MicroService> listUserServices(User user) {

  Logger logger = LoggerFactory.getLogger(user.get_id().toString());

    
    List<Service> services = listServicesFull();

    List<Service> relatedList = new LinkedList();

    for (Service service : services) {

        if (service.getAccessStrategy() != null)
            if (service.getAccessStrategy().getRequiredAttributes() != null)
                if (service.getAccessStrategy().getRequiredAttributes().get("ou") != null) {

                    Object member = service.getAccessStrategy().getRequiredAttributes().get("ou");
                    if (member != null) {
                        JSONArray s = (JSONArray) member;

                        if (user.getMemberOf() != null && s != null)
                            for (int i = 0; i < user.getMemberOf().size(); i++)
                                for (int j = 0; j < ((JSONArray) s.get(1)).size(); j++) {
                                    if (user.getMemberOf().get(i).equals(((JSONArray) s.get(1)).get(j)) && !relatedList.contains(service)) {
                                        relatedList.add(service);
                                        break;

                                    }
                                }
                    }


                }


    }
    List<MicroService> microServices = new LinkedList<>();
    MicroService microService = null;

    for (Service service : relatedList) {
        Query query = new Query(Criteria.where("_id").is(service.getId()));
        try {
            microService = mongoTemplate.findOne(query, MicroService.class, Variables.col_servicesExtraInfo);
        } catch (Exception e) {
            microService = new MicroService(service.getId(), service.getServiceId());
            logger.warn("Unable to read ExtraInfo for service " + service.getId());
        } finally {
            microServices.add(new MicroService(service, microService));
        }
    }

    Collections.sort(microServices);
    return microServices;

  }

  public List<Service> listServicesFull() {
    File folder = new File(new Settings(mongoTemplate).retrieve(Variables.SERVICE_FOLDER_PATH).getValue()); // ./services/
    String[] files = folder.list();
    List<Service> services = new LinkedList<>();
    Service service = null;
    if(files!=null)
    for (String file : Objects.requireNonNull(files)) {
      if (file.endsWith(".json"))
        try {
          service = new Analyze(mongoTemplate, new RetrieveService(mongoTemplate), uniformLogger)
              .analyze(file);
          services.add(service);
          Collections.sort(services);
        } catch (Exception e) {
          uniformLogger.warn(Variables.DOER_SYSTEM,
              new ReportMessage(Variables.MODEL_SERVICE, service.getId(),
                  "", Variables.ACTION_RETRIEVE, Variables.RESULT_FAILED, "Unable to read service"));
        }
    }
    return services;
  }

  public List<Service> listServicesWithGroups(String ou) {
    List<Service> allServices = listServicesFull();

    List<Service> relatedList = new LinkedList<>();

    for (Service service : allServices) {
      JSONArray o = (JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou");

      if (o == null || o.size() == 0)
        return null;
      val attributes = (List<String>) o.get(1);

      if (attributes.contains(ou)) {
        relatedList.add(service);
      }
    }
    return relatedList;
  }

  @Override
  public List<SimpleService> listServicesMain(String page , String count) {
    Query query = new Query();
    
    if (!page.equals("") && !page.equals(""))
      query.skip(Integer.parseInt(page)).limit(Integer.parseInt(count));
      
    return mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "position"))
    , SimpleService.class, Variables.col_servicesExtraInfo);
  }

  @Override
  public Service retrieveService(long serviceId) {

    ExtraInfo extraInfo;

    for (Service service : listServicesFull())
      if (service.getId() == serviceId) {

        Query query = new Query(Criteria.where("_id").is(service.getId()));
        try {
          extraInfo = mongoTemplate.findOne(query, ExtraInfo.class, Variables.col_servicesExtraInfo);
        } catch (Exception e) {
          extraInfo = new ExtraInfo();
          e.printStackTrace();
          uniformLogger.warn(Variables.DOER_SYSTEM,
              new ReportMessage(Variables.MODEL_SERVICE, service.getServiceId(),
                  "", Variables.ACTION_RETRIEVE, Variables.RESULT_FAILED,
                  "Unable to get extraInfo of service"));

        }

        service.setExtraInfo(extraInfo);
        return service;
      }
    return null;
  }

  @Override
  public boolean serviceAccess(long id) {
    return false;
  }

  @Override
  public String showServicePic(HttpServletResponse response, String file) {
    return null;
  }
}

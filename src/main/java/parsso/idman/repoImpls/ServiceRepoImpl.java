package parsso.idman.repoImpls;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.val;
import org.apache.commons.compress.utils.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.service.*;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.Time;
import parsso.idman.models.services.*;
import parsso.idman.models.services.serviceType.MicroService;
import parsso.idman.models.services.servicesSubModel.ExtraInfo;
import parsso.idman.models.users.User;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.ServiceRepo;
import parsso.idman.repos.UserRepo;
import parsso.idman.utils.other.GenerateUUID;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@SuppressWarnings("ALL")
@org.springframework.stereotype.Service
public class ServiceRepoImpl implements ServiceRepo {
    final String collection = Variables.col_servicesExtraInfo;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CasServiceHelper casServiceHelper;
    @Autowired
    SamlServiceHelper samlServiceHelper;
    @Autowired
    OAuthServiceHelper oAuthServiceHelper;
    @Autowired
    FilesStorageService storageService;
    @Autowired
    LdapTemplate ldapTemplate;
    @Autowired
    Position position;
    @Autowired
    UniformLogger uniformLogger;
    @Value("${base.url}")
    private String baseUrl;


    private String getServicesFolder(){
        return new Settings(mongoTemplate).retrieve(Variables.SERVICE_FOLDER_PATH).getValue().toString();
    }

    @Override
    public List<MicroService> listUserServices(User user) {

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
                                        if (user.getMemberOf().get(i).equals(((JSONArray) s.get(1)).get(j)) && !relatedList.contains(service)) {
                                            relatedList.add(service);
                                            break;

                                        }
                                    }
                        }


                    }


            try {
                if (((List<String>) (((JSONArray) (Objects.requireNonNull(service.getAccessStrategy()).getRequiredAttributes().get("uid"))).get(1))).contains(user.getUserId()))
                    relatedList.add(service);
            } catch (NullPointerException ignored) {

            }


        }
        List<MicroService> microServices = new LinkedList<>();
        MicroService microService = null;

        for (Service service : relatedList) {
            Query query = new Query(Criteria.where("_id").is(service.getId()));
            try {
                microService = mongoTemplate.findOne(query, MicroService.class, collection);
            } catch (Exception e) {
                e.printStackTrace();
                microService = new MicroService(service.getId(), service.getServiceId());
                uniformLogger.error(Variables.DOER_SYSTEM, new ReportMessage(Variables.MODEL_SERVICE, service.getId(), "",
                        Variables.ACTION_RETRIEVE, Variables.RESULT_FAILED, "unable read extra info from mongo"));

            } finally {
                MicroService fMicro = new MicroService(service, microService);

                try {
                    ServiceGist s = new Notifs().getNotifications(user.getUserId(),
                            service.getExtraInfo().getNotificationApiURL(), service.getExtraInfo().getNotificationApiKey());
                    fMicro.setNotification(new Notifs().getNotifications(user.getUserId(),
                            service.getExtraInfo().getNotificationApiURL(), service.getExtraInfo().getNotificationApiKey()));
                } catch (Exception ignored) {
                }

                microServices.add(fMicro);

            }
        }

        Collections.sort(microServices);
        return microServices;

    }

    @Override
    public List<Service> listServicesFull() {
        File folder = new File(getServicesFolder()); // ./services/
        String[] files = folder.list();
        List<Service> services = new LinkedList<>();
        Service service = null;
        for (String file : Objects.requireNonNull(files)) {
            if (file.endsWith(".json"))
                try {
                    service = analyze(file);
                    services.add(service);
                    Collections.sort(services);
                } catch (Exception e) {
                    uniformLogger.warn(Variables.DOER_SYSTEM, new ReportMessage(Variables.MODEL_SERVICE, service.getId(),
                            "", Variables.ACTION_RETRIEVE, Variables.RESULT_FAILED, "Unable to read service"));
                }
        }
        return services;
    }

    @Override
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
    public List<MicroService> listServicesMain() {

        File folder = new File(getServicesFolder()); // ./services/
        String[] files = folder.list();
        List<MicroService> services = new LinkedList<>();
        Service service = null;
        MicroService microService = null;
        for (String file : Objects.requireNonNull(files)) {
            if (file.endsWith(".json"))
                try {
                    service = analyze(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    uniformLogger.warn(Variables.DOER_SYSTEM, new ReportMessage(Variables.MODEL_SERVICE, Objects.requireNonNull(service).getId(), "",
                            Variables.ACTION_RETRIEVE, Variables.RESULT_FAILED, "Unable to parse service"));

                    continue;
                }
            Query query = new Query(Criteria.where("_id").is(Trim.extractIdFromFile(file)));
            try {
                microService = mongoTemplate.findOne(query, MicroService.class, collection);
            } catch (Exception e) {
                microService = new MicroService(Objects.requireNonNull(service).getId(), service.getServiceId());
            } finally {
                services.add(new MicroService(Objects.requireNonNull(service), microService));
            }
        }
        Collections.sort(services);
        return services;
    }

    @Override
    public Service retrieveService(long serviceId) {

        ExtraInfo extraInfo;

        for (Service service : listServicesFull())
            if (service.getId() == serviceId) {

                Query query = new Query(Criteria.where("_id").is(service.getId()));
                try {
                    extraInfo = mongoTemplate.findOne(query, ExtraInfo.class, collection);
                } catch (Exception e) {
                    extraInfo = new ExtraInfo();
                    e.printStackTrace();
                    uniformLogger.warn(Variables.DOER_SYSTEM, new ReportMessage(Variables.MODEL_SERVICE, service.getServiceId(),
                            "", Variables.ACTION_RETRIEVE, Variables.RESULT_FAILED, "Unable to get extraInfo of service"));

                }

                service.setExtraInfo(extraInfo);
                return service;
            }
        return null;
    }

    @Override
    public LinkedList<String> deleteServices(String doerID, JSONObject jsonObject) {

        File folder = new File(getServicesFolder());
        List<String> allFiles = Arrays.asList(Objects.requireNonNull(folder.list()));
        List selectedFiles = new LinkedList();

        if (jsonObject.size() == 0)
            selectedFiles = allFiles;

        else {
            ArrayList<String> files = (ArrayList<String>) jsonObject.get("names");
            for (String file : files)
                for (Object aFile : allFiles)
                    if (aFile.toString().contains(file))
                        selectedFiles.add(aFile);

        }

        LinkedList<String> notDeleted = null;

        for (Object file : selectedFiles) {
            File serv = new File(getServicesFolder() + file.toString());
            if (!(serv.delete()))
                Objects.requireNonNull(notDeleted).add((String) file);

        }

        for (Object file : selectedFiles) {
            long id = Trim.extractIdFromFile(file.toString());
            Query query = new Query(Criteria.where("_id").is(id));
            MicroService microService = mongoTemplate.findOne(query, MicroService.class, collection);
            position.delete(Objects.requireNonNull(microService).getPosition());
            try {
                mongoTemplate.remove(query, MicroService.class, collection);
                uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_SERVICE, id, "",
                        Variables.ACTION_DELETE, Variables.RESULT_SUCCESS, ""));

            } catch (Exception e) {
                uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_SERVICE, id, "",
                        Variables.ACTION_DELETE, Variables.RESULT_FAILED, "Writing to mongoDB"));

            }

        }

        return notDeleted;
    }

    @Override
    public void updateOuIdChange(String doerID, Service service, long sid, String name, String oldOu, String newOu) throws IOException {

        //Update ou
        userRepo.updateUsersWithSpecificOU(doerID, oldOu, newOu);

        //Update text
        String fileName = String.valueOf(sid);
        String s1 = fileName.replaceAll("\\s+", "");
        String filePath = name + "-" + sid + ".json";

        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        String jsonString = mapper.writeValueAsString(service);

        try {
            FileWriter file = new FileWriter(getServicesFolder() + filePath, false);
            file.write(jsonString);
            file.close();
            uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_SERVICE, sid, "", Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, service, ""));
        } catch (Exception e) {
            e.printStackTrace();
            uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, sid, "", Variables.ACTION_UPDATE, Variables.RESULT_FAILED, service, ""));

        }

    }

    @Override
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

        extraInfo.setUrl(jsonExtraInfo.get("url") != null ?
                jsonExtraInfo.get("url").toString() : jsonObject.get("serviceId").toString());

        if (baseUrl.contains("localhost"))
            extraInfo.setUUID(GenerateUUID.getUUID());


        if (system.equalsIgnoreCase("cas"))
            id = casServiceHelper.create(doerID, jsonObject);

        else if (system.equalsIgnoreCase("saml"))
            id = samlServiceHelper.create(doerID, jsonObject);

        else if (system.equalsIgnoreCase("OAuth"))
            id = oAuthServiceHelper.create(doerID, jsonObject);

        if (dailyAccess != null) {

            extraInfo.setDailyAccess((List<Schedule>) jsonExtraInfo.get("dailyAccess"));

            LinkedHashMap jsonObjectTemp = (LinkedHashMap) jsonObject.get("accessStrategy");
            jsonObject.remove("accessStrategy");
            jsonObjectTemp.remove("@class");
            jsonObjectTemp.put("@class", "org.apereo.cas.services.RemoteEndpointServiceAccessStrategy");
            jsonObjectTemp.put("endPointUrl", baseUrl + "/api/serviceCheck/" + id);
            jsonObjectTemp.put("acceptableResponseCodes", "200");
            jsonObject.put("accessStrategy", jsonObjectTemp);
            updateService("System", id, jsonObject, system);

        }


        String jsonString = new Gson().toJson(jsonObject.get("extraInfo"), Map.class);

        JSONParser parser = new JSONParser();
        JSONObject jsonObjectExtraInfo = (JSONObject) parser.parse(jsonString);

        extraInfo = setExtraInfo(id, extraInfo, jsonObjectExtraInfo, position.lastPosition() + 1);

        try {
            mongoTemplate.save(extraInfo, collection);

            return id;
        } catch (Exception e) {
            e.printStackTrace();
            uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "", Variables.ACTION_CREATE, Variables.RESULT_FAILED, "Writing to mongoDB"));
            return 0;
        }

    }

    private ExtraInfo setExtraInfo(long id, ExtraInfo extraInfo, JSONObject jsonObject, int i) {
        extraInfo.setId(id);
        extraInfo.setPosition(i);
        try {
            extraInfo.setNotificationApiURL(jsonObject.get("notificationApiURL").toString());
        } catch (Exception ignored) {
        }

        try {
            extraInfo.setNotificationApiKey(jsonObject.get("notificationApiKey").toString());
        } catch (Exception ignored) {
        }

        try {
            extraInfo.setDailyAccess((List<Schedule>) jsonObject.get("dailyAccess"));
        } catch (Exception ignored) {
        }
        return extraInfo;
    }

    @Override
    public String uploadMetadata(MultipartFile file) {
        Date date = new Date();
        String fileName = date.getTime() + "_" + file.getOriginalFilename();

        try {
            storageService.saveMetadata(file, fileName);
            return baseUrl + "/api/public/metadata/" + fileName;
            //return uploadedFilesPath+userId+timeStamp+file.getOriginalFilename();

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public HttpStatus updateService(String doerID, long id, JSONObject jsonObject, String system) {

        JSONObject JsonExtraInfo = null;

        ExtraInfo extraInfo = new ExtraInfo();

        Query query = new Query(Criteria.where("_id").is(id));

        ExtraInfo oldExtraInfo = mongoTemplate.findOne(query, ExtraInfo.class, collection);

        if (jsonObject.get("extraInfo") != null) {

            if (jsonObject.get("extraInfo").getClass().toString().equals("class org.json.simple.JSONObject"))
                JsonExtraInfo = (JSONObject) jsonObject.get("extraInfo");

            else if (jsonObject.get("extraInfo").getClass().toString().equals("class java.util.LinkedHashMap"))
                JsonExtraInfo = new JSONObject((Map) jsonObject.get("extraInfo"));

            extraInfo.setUrl(JsonExtraInfo != null && JsonExtraInfo.get("url") != null ?
                    JsonExtraInfo.get("url").toString() : jsonObject.get("serviceId").toString());

            extraInfo.setUrl(Objects.requireNonNull(JsonExtraInfo).get("url") != null ? JsonExtraInfo.get("url").toString() : Objects.requireNonNull(oldExtraInfo).getUrl());

            extraInfo.setNotificationApiURL((String) JsonExtraInfo.get("notificationApiURL"));

            extraInfo.setNotificationApiKey((String) JsonExtraInfo.get("notificationApiKey"));

            if (JsonExtraInfo.get("dailyAccess") != null) {

                extraInfo.setDailyAccess((List<Schedule>) JsonExtraInfo.get("dailyAccess"));

                LinkedHashMap jsonObjectTemp = (LinkedHashMap) jsonObject.get("accessStrategy");
                jsonObject.remove("accessStrategy");
                jsonObjectTemp.remove("@class");
                jsonObjectTemp.put("@class", "org.apereo.cas.services.RemoteEndpointServiceAccessStrategy");
                jsonObjectTemp.put("endPointUrl", baseUrl + "/api/serviceCheck/" + id);
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
                mongoTemplate.save(extraInfo, collection);

            } catch (Exception e) {
                e.printStackTrace();
                uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
                        Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "writing to mongoDB"));
                return HttpStatus.FORBIDDEN;
            }
            return casServiceHelper.update(doerID, id, jsonObject);

        } else if (system.equalsIgnoreCase("saml")) {
            try {
                mongoTemplate.save(extraInfo, collection);

            } catch (Exception e) {
                e.printStackTrace();
                uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
                        Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "writing to mongoDB"));
                return HttpStatus.FORBIDDEN;
            }

            return samlServiceHelper.update(doerID, id, jsonObject);
        } else if (system.equalsIgnoreCase("OAuth"))
            try {
                mongoTemplate.save(extraInfo, collection);

            } catch (Exception e) {
                e.printStackTrace();
                uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
                        Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "writing to mongoDB"));
                return HttpStatus.FORBIDDEN;
            }

        return oAuthServiceHelper.update(doerID, id, jsonObject);

    }

    @Override
    public HttpStatus increasePosition(String id) {
        return position.increase(id);
    }

    @Override
    public HttpStatus decreasePosition(String id) {
        return position.decrease(id);
    }


    @Override
    public boolean serviceAccess(long id) {

        Calendar c1 = Calendar.getInstance();
        int currentDay = c1.get(Calendar.DAY_OF_WEEK);

        ExtraInfo extraInfo = mongoTemplate.findOne
                (new Query(Criteria.where("_id").is(id)), ExtraInfo.class, Variables.col_servicesExtraInfo);

        Time time = new Time().longToPersianTime(new Date().getTime());
        SimpleTime simpleTime = new SimpleTime(time);
        List<Schedule> dailyAccess;
        try {
            dailyAccess = Objects.requireNonNull(extraInfo).getDailyAccess();
        } catch (NullPointerException e) {
            return false;
        }
        for (Schedule schedule : dailyAccess)
            if (schedule.getWeekDay() == currentDay) {
                Period period = schedule.getPeriod();
                if (simpleTime.compareTo(period.getFrom()) == 1 && simpleTime.compareTo(period.getTo()) == -1)
                    return true;
            }
        return false;
    }

    @Override
    public String uploadIcon(MultipartFile file) {
        Date date = new Date();
        String fileName = date.getTime() + "_" + file.getOriginalFilename();

        try {
            storageService.saveIcon(file, fileName);
            return baseUrl + "/api/public/icon/" + fileName;
            //return uploadedFilesPath+userId+timeStamp+file.getOriginalFilename();

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String showServicePic(HttpServletResponse response, String fileName) {

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

    private Service analyze(String file) throws IOException, ParseException {

        FileReader reader = new FileReader(getServicesFolder() + file);
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(reader);
        reader.close();

        Service service = null;
        ExtraInfo extraInfo;

        if (isCasService((JSONObject) obj))
            service = casServiceHelper.analyze(file);
        else if (isOAuthService((JSONObject) obj))
            service = oAuthServiceHelper.analyze(file);
        else if (isSamlService((JSONObject) obj))
            service = samlServiceHelper.analyze(file);


        Query query = new Query(Criteria.where("_id").is(Objects.requireNonNull(service).getId()));
        try {
            extraInfo = mongoTemplate.findOne(query, ExtraInfo.class, collection);
        } catch (Exception e) {
            extraInfo = new ExtraInfo();
            e.printStackTrace();
            uniformLogger.warn("System", new ReportMessage(Variables.MODEL_SERVICE, extraInfo.getId(), "",
                    Variables.ACTION_PARSE, Variables.RESULT_FAILED, "Unable to get extra service for service"));

        }

        service.setExtraInfo(extraInfo);

        return service;
    }

    private boolean isSamlService(JSONObject jo) {
        return jo.get("@class").toString().equals("org.apereo.cas.support.saml.services.SamlRegisteredService");
    }

    private boolean isOAuthService(JSONObject jo) {
        return jo.get("@class").toString().equals("org.apereo.cas.support.oauth.services.OAuthRegisteredService");
    }

    boolean isCasService(JSONObject jo) {

        return jo.get("@class").toString().equals("org.apereo.cas.services.RegexRegisteredService");

    }

}

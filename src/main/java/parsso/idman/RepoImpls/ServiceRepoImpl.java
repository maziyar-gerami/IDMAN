package parsso.idman.RepoImpls;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import parsso.idman.Helpers.Service.CasServiceHelper;
import parsso.idman.Helpers.Service.Position;
import parsso.idman.Helpers.Service.SamlServiceHelper;
import parsso.idman.Helpers.Service.Trim;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Services.ServicesSubModel.ExtraInfo;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Utils.Other.GenerateUUID;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


@org.springframework.stereotype.Service
public class ServiceRepoImpl implements ServiceRepo {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CasServiceHelper casServiceHelper;
    @Autowired
    SamlServiceHelper samlServiceHelper;
    @Autowired
    FilesStorageService storageService;
    @Autowired
    LdapTemplate ldapTemplate;
    @Autowired
    Position position;
    String collection = Variables.col_servicesExtraInfo;
    String model = "Service";
    @Value("${services.folder.path}")
    private String path;
    @Value("${base.url}")
    private String baseUrl;

    @Override
    public List<MicroService> listUserServices(User user) throws IOException {

        Logger logger = LogManager.getLogger("System");

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
                microService = mongoTemplate.findOne(query, MicroService.class, collection);
            } catch (Exception e) {
                microService = new MicroService(service.getId(), service.getServiceId());
                logger.warn(new ReportMessage(model, "", "", "retrieve", "failed", "unable read extra info from mongo for " + service.getName()).toString());

            } finally {
                microServices.add(new MicroService(service, microService));
            }
        }

        Collections.sort(microServices);
        return microServices;

    }

    @Override
    public List<Service> listServicesFull() throws IOException {
        Logger logger = LogManager.getLogger("System");
        File folder = new File(path); // ./services/
        String[] files = folder.list();
        List<Service> services = new LinkedList<>();
        for (String file : files) {
            if (file.endsWith(".json"))
                try {
                    services.add(analyze(file));
                    Collections.sort(services);
                } catch (Exception e) {
                    logger.warn(new ReportMessage(model, file, "", "retrieve", "failed", "Unable to read service ").toString());
                    continue;
                }
        }
        return services;
    }

    @Override
    public List<Service> listServicesWithGroups(String ou) throws IOException {
        List<Service> allServices = listServicesFull();

        List<Service> relatedList = new LinkedList<>();

        for (Service service : allServices) {
            JSONArray o = (JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou");

            if (o == null || o.size() == 0)
                return null;
            List<String> attributes = (List<String>) o.get(1);

            if (attributes.contains(ou)) {
                relatedList.add(service);
                continue;
            }
        }
        return relatedList;
    }

    @Override
    public List<MicroService> listServicesMain() {
        Logger logger = LogManager.getLogger("System");

        File folder = new File(path); // ./services/
        String sid;
        String[] files = folder.list();
        List<MicroService> services = new LinkedList<>();
        Service service = null;
        MicroService microService = null;
        for (String file : files) {
            if (file.endsWith(".json"))
                try {
                    service = analyze(file);
                } catch (Exception e) {
                    logger.warn(new ReportMessage(model, file, "", "retrieve", "failed", "Unable to parse service ").toString());

                    continue;
                }
            Query query = new Query(Criteria.where("_id").is(Long.valueOf(Trim.extractIdFromFile(file))));
            try {
                microService = mongoTemplate.findOne(query, MicroService.class, collection);
            } catch (Exception e) {
                microService = new MicroService(service.getId(), service.getServiceId());
            } finally {
                services.add(new MicroService(service, microService));
            }
        }
        Collections.sort(services);
        return services;
    }


    @Override
    public Service retrieveService(long serviceId) throws IOException {
        Logger logger = LogManager.getLogger("System");

        ExtraInfo extraInfo;

        for (Service service : listServicesFull())
            if (service.getId() == serviceId) {

                Query query = new Query(Criteria.where("_id").is(service.getId()));
                try {
                    extraInfo = mongoTemplate.findOne(query, ExtraInfo.class, collection);
                } catch (Exception e) {
                    extraInfo = new ExtraInfo();
                    logger.warn(new ReportMessage(model, service.getServiceId(), "", "retrieve", "failed", "Unable to get extra service for service").toString());

                }

                service.setExtraInfo(extraInfo);
                return service;
            }
        return null;
    }

    @Override
    public LinkedList<String> deleteServices(String doerID, JSONObject jsonObject) {

        Logger logger = LogManager.getLogger(doerID);

        File folder = new File(path);
        List allFiles = Arrays.asList(folder.list());
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
            File serv = new File(path + file.toString());
            if (!(serv.delete()))
                notDeleted.add((String) file);

        }

        for (Object file : selectedFiles) {
            long id = Trim.extractIdFromFile(file.toString());
            Query query = new Query(Criteria.where("_id").is(id));
            MicroService microService = mongoTemplate.findOne(query, MicroService.class, collection);
            position.delete(microService.getPosition());
            try {
                mongoTemplate.remove(query, MicroService.class, collection);
                logger.warn(new ReportMessage(model, file.toString(), "", "delete", "success", "").toString());

            } catch (Exception e) {
                logger.warn(new ReportMessage(model, file.toString(), "", "delete", "failed", "contacting mongoDB").toString());

            }

        }

        return notDeleted;
    }

    @Override
    public HttpStatus updateOuIdChange(String doerID, Service service, long sid, String name, String oldOu, String newOu) throws IOException {

        Logger logger = LogManager.getLogger(doerID);

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
            FileWriter file = new FileWriter(path + filePath, false);
            file.write(jsonString);
            file.close();
            logger.warn(new ReportMessage(model, String.valueOf(sid), "", "update", "success", "").toString());
        } catch (Exception e) {
            logger.warn(new ReportMessage(model, String.valueOf(sid), "", "update", "failed", "").toString());

        }

        return HttpStatus.OK;

    }

    String getSystem(Service service) {
        if (service.getAtClass().contains("saml"))
            return "saml";
        else if (service.getAtClass().contains("cas"))
            return "cas";

        else
            return null;
    }


    @Override
    public HttpStatus createService(String doerID, JSONObject jsonObject, String system) throws IOException {

        Logger logger = LogManager.getLogger(doerID);

        ExtraInfo extraInfo = null;
        long id = 0;
        JSONObject jsonExtraInfo = new JSONObject();


        if (jsonObject.get("extraInfo") != null) {
            if (jsonObject.get("extraInfo").getClass().toString().equals("class org.json.simple.JSONObject"))
                jsonExtraInfo = (JSONObject) jsonObject.get("extraInfo");

            else if (jsonObject.get("extraInfo").getClass().toString().equals("class java.util.LinkedHashMap"))
                jsonExtraInfo = new JSONObject((Map) jsonObject.get("extraInfo"));
        }

        extraInfo = new ExtraInfo();

        extraInfo.setUrl(jsonExtraInfo != null && jsonExtraInfo.get("url") != null ?
                jsonExtraInfo.get("url").toString() : jsonObject.get("serviceId").toString());

        if (baseUrl.contains("localhost")) {
            extraInfo.setUUID(GenerateUUID.getUUID());
        }

        if (system.equalsIgnoreCase("cas")) {
            id = casServiceHelper.create(jsonObject);
            if (id > 0) {
                if (extraInfo != null) {
                    extraInfo.setId(id);
                    extraInfo.setPosition(position.lastPosition() + 1);

                } else {
                    extraInfo = new ExtraInfo();
                    extraInfo.setId(id);
                    extraInfo.setPosition(position.lastPosition() + 1);

                }

            }
        } else if (system.equalsIgnoreCase("saml"))
            id = samlServiceHelper.create(doerID, jsonObject);
        if (id > 0)
            if (extraInfo != null) {
                extraInfo.setId(id);
                extraInfo.setPosition(position.lastPosition() + 1);

            } else {
                extraInfo = new ExtraInfo();
                extraInfo.setId(id);
                extraInfo.setPosition(position.lastPosition() + 1);
            }


        try {
            mongoTemplate.save(extraInfo, collection);
            logger.warn(new ReportMessage(model, String.valueOf(extraInfo.getId()), "", "create", "success", "").toString());
            return HttpStatus.OK;
        } catch (Exception e) {
            logger.warn(new ReportMessage(model, String.valueOf(extraInfo.getId()), "", "create", "failed", "writing to mongoDB").toString());
            return HttpStatus.FORBIDDEN;
        }

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
    public HttpStatus updateService(String doerID, long id, JSONObject jsonObject, String system) throws IOException, ParseException {

        Logger logger = LogManager.getLogger(doerID);

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

            extraInfo.setUrl(JsonExtraInfo.get("url") != null ? JsonExtraInfo.get("url").toString() : oldExtraInfo.getUrl());
            try {
                extraInfo.setPosition(oldExtraInfo.getPosition());

            } catch (Exception e) {

            }
            extraInfo.setId(id);


        }


        if (system.equalsIgnoreCase("cas")) {
            try {
                mongoTemplate.save(extraInfo, collection);

            } catch (Exception e) {
                logger.warn(new ReportMessage(model, String.valueOf(extraInfo.getId()), "", "update", "failed", "writing to mongoDB").toString());
                return HttpStatus.FORBIDDEN;
            }
            return casServiceHelper.update(id, jsonObject);

        } else if (system.equalsIgnoreCase("saml"))
            try {
                mongoTemplate.save(extraInfo, collection);

            } catch (Exception e) {
                logger.warn(new ReportMessage(model, String.valueOf(extraInfo.getId()), "", "update", "failed", "writing to mongoDB").toString());
                return HttpStatus.FORBIDDEN;
            }
        return samlServiceHelper.update(doerID, id, jsonObject);

    }

    @Override
    public HttpStatus increasePosition(String id) {
        return position.increase(id);
    }

    @Override
    public HttpStatus decreasePosition(String id) {
        return position.decrease(id);
    }

    private Service analyze(String file) throws IOException, ParseException {
        Logger logger = LogManager.getLogger("System");

        FileReader reader = new FileReader(path + file);
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(reader);
        reader.close();

        Service service;
        ExtraInfo extraInfo;

        if (isCasService((JSONObject) obj))
            service = casServiceHelper.analyze(file);
        else
            service = samlServiceHelper.analyze(file);

        Query query = new Query(Criteria.where("_id").is(service.getId()));
        try {
            extraInfo = mongoTemplate.findOne(query, ExtraInfo.class, collection);
        } catch (Exception e) {
            extraInfo = new ExtraInfo();
            logger.warn(new ReportMessage(model, String.valueOf(extraInfo.getId()), "", "parse", "failed", "Unable to get extra service for service").toString());

        }

        service.setExtraInfo(extraInfo);

        return service;
    }

    boolean isCasService(JSONObject jo) {

        return !jo.get("@class").toString().contains("saml");

    }

}

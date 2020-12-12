package parsso.idman.RepoImpls;


import com.google.gson.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Helpers.Service.CasServiceHelper;
import parsso.idman.Helpers.Service.Position;
import parsso.idman.Helpers.Service.SamlServiceHelper;
import parsso.idman.Helpers.Service.Trim;
import parsso.idman.Models.Service;
import parsso.idman.Models.ServiceType.MicroService;
import parsso.idman.Models.ServicesSubModel.ExtraInfo;
import parsso.idman.Models.User;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.ServiceRepo;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


@org.springframework.stereotype.Service
public class ServiceRepoImpl implements ServiceRepo {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    CasServiceHelper casServiceHelper;
    @Autowired
    SamlServiceHelper samlServiceHelper;
    Logger logger = LoggerFactory.getLogger(ServiceRepoImpl.class);
    @Value("${services.folder.path}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    @Value("${metadata.file.path}")
    private String uploadedFilesPath;


    @Autowired
    FilesStorageService storageService;

    @Autowired
    Position position;

    String collection = "IDMAN_ServicesExtraInfo";


    @Override
    public List<MicroService> listUserServices(User user) throws IOException {
        File folder = new File(path); // ./services/
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

        for (Service service : relatedList) {
            microServices.add(new MicroService(service.getId(), service.getName(), service.getServiceId(), service.getDescription(), service.getLogo()));
        }

        Collections.sort(microServices);
        return microServices;

    }

    @Override
    public List<Service> listServicesFull() throws IOException {
        File folder = new File(path); // ./services/
        String[] files = folder.list();
        List<Service> services = new LinkedList<>();
        for (String file : files) {
            if (file.endsWith(".json"))
                try {
                    services.add(analyze(file));
                    Collections.sort(services);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn("Unable to read service "+file);
                    continue;
                }
        }
        return services;
    }

    @Override
    public List<MicroService> listServicesMain() throws IOException, ParseException {
        File folder = new File(path); // ./services/
        String sid;
        String[] files = folder.list();
        List<MicroService> services = new LinkedList<>();
        for (String file : files) {
            if (file.endsWith(".json"))
                try {
                    Service service = analyze(file);
                    Query query = new Query(Criteria.where("_id").is(Long.valueOf(Trim.extractIdFromFile(file))));
                    MicroService microService = mongoTemplate.findOne(query,MicroService.class,collection);
                    services.add(new MicroService(service,microService));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn("Unable to read service "+file);
                    continue;
                }
        }
        Collections.sort(services);
        return services;
    }



    @Override
    public Service retrieveService(long serviceId) throws IOException {

        for (Service service : listServicesFull())
            if (service.getId() == serviceId)
                return service;

        return null;
    }

    @Override
    public LinkedList<String> deleteServices(JSONObject jsonObject) {
        File folder = new File(path);
        List allFiles = Arrays.asList(folder.list());
        List selectedFiles = new LinkedList();

        if (jsonObject.size()==0)
            selectedFiles = allFiles;

        else {
            ArrayList<String> files = (ArrayList<String>) jsonObject.get("names");
            for (String file : files)
                for (Object aFile : allFiles)
                    if (aFile.toString().contains(file))
                        selectedFiles.add(aFile);


        }

        LinkedList<String> notDeleted = null;
        List<String> toBeDeleted = new LinkedList();

        for (Object file : selectedFiles) {
            File serv = new File(path + file.toString());
            if (!(serv.delete())) {
                notDeleted.add((String) file);

                logger.warn("Deleting Service " + "\""+file+ "\"" + " was unsuccessful");
            }

        }

        for (Object file: selectedFiles) {
            long id = Trim.extractIdFromFile(file.toString());
            Query query = new Query(Criteria.where("_id").is(id));
            MicroService microService = mongoTemplate.findOne(query, MicroService.class,collection);
            position.delete(microService.getPosition());
            mongoTemplate.remove(query, MicroService.class,collection);

        }

        return notDeleted;
    }



    @Override
    public HttpStatus createService(JSONObject jsonObject, String system) throws IOException {

        ExtraInfo extraInfo = null;
        long id = 0;


        if (jsonObject.get("extraInfo") != null) {

            JSONObject jsonExtraInfo = new JSONObject();

            if (jsonObject.get("extraInfo").getClass().toString().equals("class org.json.simple.JSONObject"))
                jsonExtraInfo = (JSONObject) jsonObject.get("extraInfo");

            else if (jsonObject.get("extraInfo").getClass().toString().equals("class java.util.LinkedHashMap"))
                jsonExtraInfo = new JSONObject((Map) jsonObject.get("extraInfo"));

            extraInfo = new ExtraInfo();
            if (jsonExtraInfo.get("url")!=null)
                extraInfo.setUrl(jsonExtraInfo.get("url").toString());


        }


        if (system.equalsIgnoreCase("cas")) {
            id = casServiceHelper.create(jsonObject);
            if (id > 0)
                if (extraInfo != null) {
                    extraInfo.setId(id);
                    extraInfo.setPosition(position.lastPosition()+1);
                    mongoTemplate.save(extraInfo, collection);
                    return HttpStatus.OK;
                } else {
                    extraInfo = new ExtraInfo();
                    extraInfo.setId(id);
                    extraInfo.setPosition(position.lastPosition()+1);
                    mongoTemplate.save(extraInfo, collection);
                    return HttpStatus.OK;
                }
        }

        else if (system.equalsIgnoreCase("saml"))
            id = samlServiceHelper.create(jsonObject);
                if (id>0)
                    if(extraInfo!=null) {
                        extraInfo.setId(id);
                        extraInfo.setPosition(position.lastPosition()+1);
                        mongoTemplate.save(extraInfo,collection);
                        return HttpStatus.OK;
                    }
                    else
                 {
                    extraInfo = new ExtraInfo();
                    extraInfo.setId(id);
                     extraInfo.setPosition(position.lastPosition()+1);
                     mongoTemplate.save(extraInfo, collection);
                    return HttpStatus.OK;
                }

        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String uploadMetadata(MultipartFile file) {
        Date date = new Date();
        String fileName = date.getTime()+"_"+file.getOriginalFilename();

        try {
            storageService.saveMetadata(file, fileName);
            return baseUrl+"/api/public/metadata/"+fileName;
            //return uploadedFilesPath+userId+timeStamp+file.getOriginalFilename();

        }catch (Exception e){
            return null;
        }
    }


    @Override
    public HttpStatus updateService(long id, JSONObject jsonObject, String system) throws IOException, ParseException {
        if (system.equalsIgnoreCase("cas"))
            return casServiceHelper.update(id, jsonObject);

        else if (system.equalsIgnoreCase("saml"))
            return samlServiceHelper.update(id, jsonObject);

        return HttpStatus.FORBIDDEN;
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
        FileReader reader = new FileReader(path + file);
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(reader);
        reader.close();

        if (isCasService((JSONObject) obj))
            return casServiceHelper.analyze(file);
        else
            return samlServiceHelper.analyze(file);
    }

    boolean isCasService(JSONObject jo) {

        if (jo.get("@class").toString().contains("saml"))
            return false;
        return true;

    }

}

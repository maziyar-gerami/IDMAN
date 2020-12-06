package parsso.idman.RepoImpls;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Helpers.Service.CasServiceHelper;
import parsso.idman.Helpers.Service.SamlServiceHelper;
import parsso.idman.Models.Service;
import parsso.idman.Models.ServiceType.MicroService;
import parsso.idman.Models.User;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.ServiceRepo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    @Value("${metadata.file.path}")
    private String uploadedFilesPath;

    @Autowired
    FilesStorageService storageService;


    @Override
    public List<MicroService> listUserServices(User user) throws IOException {
        File folder = new File(path); // ./services/
        List<Service> services = listServices();

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

        return microServices;

    }

    @Override
    public List<Service> listServices() throws IOException {
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
    public Service retrieveService(long serviceId) throws IOException {

        for (Service service : listServices())
            if (service.getId() == serviceId)
                return service;

        return null;
    }

    @Override
    public HttpStatus deleteService(long serviceId)  {
        File folder = new File(path);

        for (String file : folder.list()) {
            if (file.contains(String.valueOf(serviceId))) {
                File oFile = new File(path + file);

                if (!oFile.exists()) {
                    logger.warn("File related to service "+"\""+serviceId+"\""+" with "+"\""+file+"\""+ " file name, no longer exist");
                    return HttpStatus.BAD_REQUEST;

                }
                else if (oFile.delete()) {
                    logger.info("Service "+"\""+serviceId+"\""+" with "+"\""+file+"\""+ " file name, deleted successfully");
                    return HttpStatus.OK;
                }
                else {
                    logger.warn("Deleting Service " + "\""+serviceId+ "\"" + " was unsuccessful");
                    return HttpStatus.FORBIDDEN;
                }

            }
        }
        return HttpStatus.BAD_REQUEST;
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
        for (Object file : selectedFiles) {
            File serv = new File(path + file.toString());
            if (!(serv.delete())) {
                notDeleted.add((String) file);
                logger.warn("Deleting Service " + "\""+file+ "\"" + " was unsuccessful");

            }
        }

        return notDeleted;
    }

    @Override
    public HttpStatus createService(JSONObject jsonObject, String system) throws IOException {
        if (system.equalsIgnoreCase("cas"))
            return casServiceHelper.create(jsonObject);

        else if (system.equalsIgnoreCase("saml"))
            return samlServiceHelper.create(jsonObject);

        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String uploadMetadata(MultipartFile file, String userId) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(System.currentTimeMillis());

        try {
            storageService.save(file, userId+timeStamp+file.getOriginalFilename(),uploadedFilesPath);
            return uploadedFilesPath+userId+timeStamp+file.getOriginalFilename();

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

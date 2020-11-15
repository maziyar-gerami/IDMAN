package parsso.idman.RepoImpls;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import parsso.idman.Helpers.Service.CasServiceHelper;
import parsso.idman.Helpers.Service.SamlServiceHelper;
import parsso.idman.Models.Service;
import parsso.idman.Models.ServiceType.CasService;
import parsso.idman.Models.User;
import parsso.idman.Repos.ServiceRepo;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


@org.springframework.stereotype.Service
public class ServiceRepoImpl implements ServiceRepo {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    CasServiceHelper casServiceHelper;
    @Autowired
    SamlServiceHelper samlServiceHelper;
    @Value("${services.folder.path}")
    private String path;

    @Override
    public List<Service> listUserServices(User user) throws IOException {
        File folder = new File(path); // ./services/
        List<Service> services = listServices();

        List<Service> relatedList = new LinkedList();

        for (Service service : services) {

            if(service.getAccessStrategy()!=null)
                if(service.getAccessStrategy().getRequiredAttributes()!=null)
                    if (service.getAccessStrategy().getRequiredAttributes().get("ou")!=null) {

                        Object member = service.getAccessStrategy().getRequiredAttributes().get("ou");
                        if (member !=null){
                            JSONArray s = (JSONArray) member;


                            if (user.getMemberOf()!=null&&s !=null)
                                for (int i = 0; i < user.getMemberOf().size(); i++)
                                    for (int j = 0; j < ((JSONArray)s.get(1)).size(); j++) {
                                        if (user.getMemberOf().get(i).equals(((JSONArray) s.get(1)).get(j)) && !relatedList.contains(service)) {
                                            relatedList.add(service);
                                            break;

                                        }
                                    }
                        }


                    }


        }

        return relatedList;

    }

    @Override
    public List<Service> listServices() throws IOException {
        File folder = new File(path); // ./services/
        String[] files = folder.list();
        JSONParser jsonParser = new JSONParser();
        List<Service> services = new LinkedList<>();
        for (String file : files) {
            if (file.endsWith(".json"))
                try {
                     services.add(analyze(file));
                } catch (Exception e) {
                    continue;
                }
        }
        return services;
    }


    @Override
    public Service retrieveService(long serviceId) throws IOException {

        for (Service service : listServices()) {
            if (service.getId() == serviceId)
                return service;

        }

        return null;
    }

    @Override
    public HttpStatus deleteService(long serviceId) throws IOException, ParseException {
        File folder = new File(path);
        String[] files = folder.list();
        for (String file : files) {
            File serv = new File((path + file));
            if (file.endsWith(".json")) {

                Service service = analyze(file);
                if (serviceId == service.getId()) {
                    try {
                        if (serv.delete())

                            return HttpStatus.OK;

                        else if (!serv.exists())
                            return HttpStatus.NOT_EXTENDED;
                        else
                            return HttpStatus.FORBIDDEN;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public HttpStatus deleteServices() {
        File folder = new File(path);
        String[] files = folder.list();
        for (String file : files) {
            File serv = new File(path + file);
            if (!(serv.delete()))
                return HttpStatus.FORBIDDEN;

        }
        return HttpStatus.OK;

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
    public HttpStatus updateService(long id, JSONObject jsonObject, String system) throws IOException, ParseException {
        if (system.equalsIgnoreCase("cas"))
            return casServiceHelper.update(id, jsonObject);

        else if (system.equalsIgnoreCase("saml"))
            return samlServiceHelper.update(id, jsonObject);

        return HttpStatus.FORBIDDEN;
    }

    private Service analyze(String file) throws IOException, ParseException {
        return casServiceHelper.analyze(file);
    }

    boolean isCasService(JSONObject jo) {

        if (jo.get("@class").toString().contains("saml"))
            return false;
        return true;

    }

}

package parsso.idman.Controllers;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import parsso.idman.Models.Person;
import parsso.idman.Repos.PersonRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Controller
public class ServicesController {

    @Qualifier("personRepoImpl")
    @Autowired
    private PersonRepo personRepo;

    @Value("${administrator.ou.name}")
    private String adminOu;

    @GetMapping("/api/service")
    public ResponseEntity<List<Object>> ListUserServices(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        Person person = personRepo.retrievePerson(principal.getName());
        List<String> memberOf = person.getMemberOf();
        File folder = new File("/etc/cas/services/"); // ./services/
        String[] files = folder.list();
        JSONParser jsonParser = new JSONParser();
        List<Object> services =  new ArrayList<Object>();
        if(files!=null)
        for (String file : files){
            try (FileReader reader = new FileReader("/etc/cas/services/" + file)){ // 
                Object obj = jsonParser.parse(reader);
                JSONObject jo = (JSONObject) obj;
                Map accessStrategy = ((Map)jo.get("accessStrategy")); 
                Map requiredAttributes = ((Map)accessStrategy.get("requiredAttributes"));
                JSONArray member = (JSONArray) requiredAttributes.get("member");
                JSONArray groups = (JSONArray) member.get(1);
                try{
                    if(groups.size() == 0){
                        continue;
                    }
                } catch (NullPointerException e){
                    continue;
                }
                try{
                    for(int i = 0; i < memberOf.size(); ++i){
                        if(groups.toString().contains(memberOf.get(i))){
                            services.add(obj);
                            break;
                        }
                    } 
                } catch (NullPointerException e){
                    break;
                }
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } catch (ParseException e){
                e.printStackTrace();
            }
        }
        return new ResponseEntity<List<Object>>(services, HttpStatus.OK);
    }

    @GetMapping("/api/services")
    public ResponseEntity<List<Object>> ListServices() {
        File folder = new File("/etc/cas/services/"); // ./services/
        String[] files = folder.list();
        JSONParser jsonParser = new JSONParser();
        List<Object> services =  new ArrayList<Object>();
        for (String file : files){
            try (FileReader reader = new FileReader("/etc/cas/services/" + file)){ // 
                Object obj = jsonParser.parse(reader);
                services.add(obj);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } catch (ParseException e){
                e.printStackTrace();
            }
        }
        return new ResponseEntity<List<Object>>(services, HttpStatus.OK);
    }

    @GetMapping("/api/services/{id}")
    public ResponseEntity<Object> retrieveService(@PathVariable("id") String serviceId){
        File folder = new File("/etc/cas/services/");
        String[] files = folder.list();
        JSONParser jsonParser = new JSONParser();
        if(files !=null)
        for (String file : files){
            if(file.contains(serviceId)){
                try (FileReader reader = new FileReader("/etc/cas/services/" + file)){
                    Object obj = jsonParser.parse(reader);
                    return new ResponseEntity<Object> (obj, HttpStatus.OK);
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @DeleteMapping("/api/services/{id}")
    public ResponseEntity<String> deleteService(@PathVariable("id") String serviceId){
        File folder = new File("/etc/cas/services/");
        String[] files = folder.list();
        for (String file : files){
            if(file.contains(serviceId)){
                File serv = new File("/etc/cas/services/" + file);
                serv.delete();
                return new ResponseEntity<>("Success", HttpStatus.OK);
            }
        }
        return null;
    }

    @DeleteMapping("/api/services")
    public ResponseEntity<String> deleteServices(){
        File folder = new File("/etc/cas/services/");
        String[] files = folder.list();
        for (String file : files){
                File serv = new File("/etc/cas/services/" + file);
                serv.delete();
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/services")
    public String Services(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            Person person = personRepo.retrievePerson(principal.getName());
            List<String> memberOf = person.getMemberOf();
            if(memberOf.contains(adminOu)){
                    return "services";
            }else{
                    return "403";
            }
        } catch (Exception e) {
            return "403";
        }
    }

    @GetMapping("/createservice")
    public String CreateService(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            Person person = personRepo.retrievePerson(principal.getName());
            List<String> memberOf = person.getMemberOf();
            if(memberOf.contains(adminOu)){
                    return "createservice";
            }else{
                    return "403";
            }
        } catch (Exception e) {
            return "403";
        }
    }

    @PostMapping("/createservice")
    public String AddService(@RequestParam Map<String, String> allParams, HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            Person person = personRepo.retrievePerson(principal.getName());
            List<String> memberOf = person.getMemberOf();
            if(memberOf.contains(adminOu)){
                JSONObject jsonObject = new JSONObject();
                File service;
                String class1 = "org.apereo.cas.services.RegexRegisteredService";
                int id;
                if(allParams.get("id").equals("")){
                    id = Integer.parseInt(LocalTime.now().toString().replace(":", "").replace(".", ""));
                }else{
                    id = Integer.parseInt(allParams.get("id"));
                }
                JSONObject accessStrategy = new JSONObject();
                String class2 = "org.apereo.cas.services.RemoteEndpointServiceAccessStrategy";
                String acceptableResponseCodes = "200,202";
                JSONObject requiredAttributes = new JSONObject();
                JSONArray groups = new JSONArray();
                JSONArray member = new JSONArray();
                String[] groupList;
                JSONArray contacts = new JSONArray();
                JSONArray contactsArray = new JSONArray();
                JSONObject contactsObject = new JSONObject();
                String class3 = "org.apereo.cas.services.DefaultRegisteredServiceContact";

                if(allParams.get("type").equals("SAML")){
                    class1 = "org.apereo.cas.support.saml.services.SamlRegisteredService";
                    class2 = "org.apereo.cas.support.saml.services.RemoteEndpointServiceAccessStrategy";
                    class3 = "org.apereo.cas.support.saml.services.DefaultRegisteredServiceContact";
                }

                jsonObject.put("@class", class1);
                jsonObject.put("id", id);
                if(!allParams.get("name").equals("")){
                    jsonObject.put("name", allParams.get("name"));
                }else{
                    return "";
                }
                if(!allParams.get("serviceId").equals("")){
                    jsonObject.put("serviceId", "^" + allParams.get("serviceId") + ".+");
                }else{
                    return "";
                }
                if(!allParams.get("description").equals("")){
                    jsonObject.put("description", allParams.get("description"));
                }
                accessStrategy.put("@class", class2);
                if(allParams.containsKey("enabled")){
                    accessStrategy.put("enabled", true);
                }else{
                    accessStrategy.put("enabled", false);
                }
                if(!allParams.get("endpointUrl").equals("")){
                    accessStrategy.put("endpointUrl", allParams.get("endpointUrl"));
                }else{
                    return "";
                }
                accessStrategy.put("acceptableResponseCodes", acceptableResponseCodes);
                requiredAttributes.put("@class", "java.util.HashMap");
                groupList = allParams.get("groups").split(",");
                for(int i = 0; i < groupList.length; ++i){
                    groups.add(i, groupList[i]);
                }
                member.add(0, "java.util.HashSet");
                member.add(1, groups);
                requiredAttributes.put("member", member);
                accessStrategy.put("requiredAttributes", requiredAttributes);
                jsonObject.put("accessStrategy", accessStrategy);
                if(!allParams.get("logo").equals("")){
                    jsonObject.put("logo", allParams.get("logo"));
                }
                if(!allParams.get("informationUrl").equals("")){
                    jsonObject.put("informationUrl", allParams.get("informationUrl"));
                }
                if(!allParams.get("privacyUrl").equals("")){
                    jsonObject.put("privacyUrl", allParams.get("privacyUrl"));
                }
                if(allParams.get("type").equals("SAML")){
                    if(!allParams.get("metadataLocation").equals("")){
                        jsonObject.put("metadataLocation", allParams.get("metadataLocation"));
                    }else{
                        return "";
                    }
                    if(!allParams.get("metadataMaxValidity").equals("")){
                        jsonObject.put("metadataMaxValidity", allParams.get("metadataMaxValidity"));
                    }
                    if(!allParams.get("metadataSignatureLocation").equals("")){
                        jsonObject.put("metadataSignatureLocation", allParams.get("metadataSignatureLocation"));
                    }
                    if(!allParams.get("metadataExpirationDuration").equals("")){
                        jsonObject.put("metadataExpirationDuration", allParams.get("metadataExpirationDuration"));
                    }
                    if(!allParams.get("metadataCriteriaPattern").equals("")){
                        jsonObject.put("metadataCriteriaPattern", allParams.get("metadataCriteriaPattern"));
                    }
                    if(!allParams.get("metadataCriteriaDirection").equals("")){
                        jsonObject.put("metadataCriteriaDirection", allParams.get("metadataCriteriaDirection"));
                    }
                    if(!allParams.get("metadataCriteriaRoles").equals("")){
                        jsonObject.put("metadataCriteriaRoles", allParams.get("metadataCriteriaRoles"));
                    }
                    if(allParams.containsKey("metadataCriteriaRemoveEmptyEntitiesDescriptors")){
                        jsonObject.put("metadataCriteriaRemoveEmptyEntitiesDescriptors", true);
                    }else{
                        jsonObject.put("metadataCriteriaRemoveEmptyEntitiesDescriptors", false);
                    }
                    if(allParams.containsKey("metadataCriteriaRemoveRolelessEntityDescriptors")){
                        jsonObject.put("metadataCriteriaRemoveRolelessEntityDescriptors", true);
                    }else{
                        jsonObject.put("metadataCriteriaRemoveRolelessEntityDescriptors", false);
                    }
                    if(allParams.containsKey("signAssertions")){
                        jsonObject.put("signAssertions", true);
                    }else{
                        jsonObject.put("signAssertions", false);
                    }
                    if(allParams.containsKey("signResponses")){
                        jsonObject.put("signResponses", true);
                    }else{
                        jsonObject.put("signResponses", false);
                    }
                    if(allParams.containsKey("encryptAssertions")){
                        jsonObject.put("encryptAssertions", true);
                    }else{
                        jsonObject.put("encryptAssertions", false);
                    }
                    if(!allParams.get("signingCredentialType").equals("")){
                        jsonObject.put("signingCredentialType", allParams.get("signingCredentialType"));
                    }
                    if(!allParams.get("requiredAuthenticationContextClass").equals("")){
                        jsonObject.put("requiredAuthenticationContextClass", allParams.get("requiredAuthenticationContextClass"));
                    }
                    if(!allParams.get("assertionAudiences").equals("")){
                        jsonObject.put("assertionAudiences", allParams.get("assertionAudiences"));
                    }
                }
                contacts.add(0, "java.util.ArrayList");
                contactsObject.put("@class", class3);
                if(!allParams.get("cname").equals("")){
                    contactsObject.put("name", allParams.get("cname"));
                }else{
                    return "";
                }
                if(!allParams.get("email").equals("")){
                    contactsObject.put("email", allParams.get("email"));
                }else{
                    return "";
                }
                if(!allParams.get("phone").equals("")){
                    contactsObject.put("phone", allParams.get("phone"));
                }
                if(!allParams.get("department").equals("")){
                    contactsObject.put("department", allParams.get("department"));
                }
                contactsArray.add(contactsObject);
                contacts.add(1, contactsArray);
                jsonObject.put("contacts", contacts);
                if(!allParams.get("logoutUrl").equals("")){
                    jsonObject.put("logoutUrl", allParams.get("logoutUrl"));
                }else{
                    return "";
                }
                if(!allParams.get("logoutType").equals("")){
                    jsonObject.put("logoutType", allParams.get("logoutType"));
                }else{
                    return "";
                }


                if(allParams.get("id").equals("")){
                    try {
                        FileWriter file = new FileWriter("/etc/cas/services/" + allParams.get("name") + "-" + id + ".json");
                        file.write(jsonObject.toJSONString().replace("\\/", "/"));
                        file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    File folder = new File("/etc/cas/services/");
                    String[] files = folder.list();
                    if(files!=null)
                    for (String file : files){
                        if(file.contains(allParams.get("id"))){
                            File serv = new File("/etc/cas/services/" + file);
                            serv.delete();
                        }
                    }
                    try {
                        FileWriter file = new FileWriter("/etc/cas/services/" + allParams.get("name") + "-" + allParams.get("id") + ".json");
                        file.write(jsonObject.toJSONString().replace("\\/", "/"));
                        file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return "redirect:/services";
            }else{
                return "403";
            }
        } catch (Exception e) {
            return "403";
        }
    }
}

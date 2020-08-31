package parsso.idman.RepoImpls;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonArray;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Service;
import parsso.idman.Models.ServicesSubModel.*;
import parsso.idman.Models.User;
import parsso.idman.Repos.ServiceRepo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


@org.springframework.stereotype.Service
public class ServiceRepoImpl implements ServiceRepo {

    public static String path;



    @Value("${services.folder.path}")
    public void setPath(String value){
        path = value;
    }



    @Override
    public List<Service> listUserServices(User user) throws IOException, ParseException {
        File folder = new File(path); // ./services/
        String[] files = folder.list();
        JSONParser jsonParser = new JSONParser();
        List<Service> services = listServices();



        List<Service> relatedList = new LinkedList();

        for (Service service : services) {

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

        return relatedList;
    }


    @Override
    public List<Service> listServices() throws IOException, ParseException {
        File folder = new File(path); // ./services/
        String[] files = folder.list();
        JSONParser jsonParser = new JSONParser();
        List<Service> services = new LinkedList<>();
        for (String file : files) {
            if(file.endsWith(".json"))
                try {
                    services.add(analyze(file));
                } catch (ParseException e) {
                    continue;
                }
        }
        return services;

    }

    @Override
    public Service retrieveService(long serviceId) throws IOException, ParseException {
        List allServices = new LinkedList();

        allServices = listServices();


        for (int i = 0; i < allServices.size(); i++) {
            Service service = (Service) allServices.get(i);
            if (service.getId() == serviceId)
                return service;
        }

        return null;
    }

    @Override
    public HttpStatus deleteService(long serviceId) throws IOException, ParseException {
        List<Service> services = listServices();
        File folder = new File(path);
        String[] files = folder.list();
        for (String file : files) {
            File serv = new File((path + file));
            if(file.endsWith(".json")) {

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
            if (serv.delete())
                return HttpStatus.OK;
            else
                return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.NOT_FOUND;
    }


    @Override
    public HttpStatus createService(JSONObject jsonObject) {

        Service service = buildService(jsonObject);
        Date date = new Date();
        service.setId(date.getTime());


        String json = null;

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            json = ow.writeValueAsString(service);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        FileWriter file = null;
        try {
            String fileName = service.getName();
            String s1 = fileName.replaceAll("\\s+", "");
            s1 = s1.replaceAll("[-,]", "");
            String filePath = s1+"-"+service.getId();

            file = new FileWriter(path+filePath + ".json");
            file.write(json);
            file.close();
            return HttpStatus.OK;
        } catch (IOException e) {
            e.printStackTrace();
            return HttpStatus.FORBIDDEN;
        }

    }

    @Override
    public HttpStatus updateService(long id, JSONObject jsonObject) throws IOException, ParseException {

        Service oldService = retrieveService(id);


        Service service = buildService(jsonObject);
        service.setId(id);


        String json = null;

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            json = ow.writeValueAsString(service);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        FileWriter file = null;
        try {

            File oldFile = new File(path+oldService.getName()+"-"+service.getId()+".json");
            oldFile.delete();
            String fileName = service.getName();
            String s1 = fileName.replaceAll("\\s+", "");
            s1 = s1.replaceAll("[-,]", "");
            String filePath = s1+"-"+service.getId();

            file = new FileWriter(path+filePath + ".json");
            file.write(json);
            file.close();
            return HttpStatus.OK;
        } catch (IOException e) {
            e.printStackTrace();
            return HttpStatus.FORBIDDEN;
        }

    }

    private Service analyze(String file) throws IOException, ParseException {

        FileReader reader = new FileReader(path + file);
        JSONParser jsonParser = new JSONParser();
        Object obj = null;

            obj = jsonParser.parse(reader);

        reader.close();
        return buildService((JSONObject) obj);

    }

    private Service buildService(JSONObject jo) {

        Service service = new Service();
        if (jo.get("id") != null)
            service.setId(Long.valueOf(jo.get("id").toString()));

        if (jo.get("serviceId") != null) service.setServiceId(jo.get("serviceId").toString());
        if (jo.get("description") != null) service.setDescription(jo.get("description").toString());
        if (jo.get("logoutType") != null) service.setLogoutType(jo.get("logoutType").toString());
        if (jo.get("@class") != null) service.setAtClass(jo.get("@class").toString());
        if (jo.get("logoutUrl") != null) service.setLogoutUrl(jo.get("logoutUrl").toString());
        if (jo.get("name") != null) service.setName(jo.get("name").toString());
        if (jo.get("privacyUrl") != null) service.setPrivacyUrl(jo.get("privacyUrl").toString());
        if (jo.get("logo") != null) service.setLogo(jo.get("logo").toString());
        if (jo.get("informationUrl") != null) service.setInformationUrl(jo.get("informationUrl").toString());




        if (jo.get("expirationPolicy") == null) {
            ExpirationPolicy expirationPolicy = new ExpirationPolicy();
            service.setExpirationPolicy(expirationPolicy);
        } else {
            JSONObject jsonObject = null;
            String s = jo.get("expirationPolicy").getClass().toString();
            if (jo.get("expirationPolicy").getClass().toString().equals("class org.json.simple.JSONObject")) {
                jsonObject = new JSONObject();
                jsonObject = (JSONObject) jo.get("expirationPolicy");
            }
            if (jo.get("expirationPolicy").getClass().toString().equals("class java.util.LinkedHashMap")) {
                jsonObject = new JSONObject((Map) jo.get("expirationPolicy"));
            }


            ExpirationPolicy expirationPolicy = new ExpirationPolicy();
            expirationPolicy.setAtClass((String) jsonObject.get("@class"));
            expirationPolicy.setDeleteWhenExpired((boolean) jsonObject.get("deleteWhenExpired"));
            expirationPolicy.setNotifyWhenDeleted((boolean) jsonObject.get("notifyWhenDeleted"));
            service.setExpirationPolicy(expirationPolicy);

        }

        if (jo.get("proxyPolicy") == null) {
            ProxyPolicy proxyPolicy = new ProxyPolicy();
            service.setProxyPolicy(proxyPolicy);
        }else {

            JSONObject jsonObject = null;
            String s = jo.get("proxyPolicy").getClass().toString();
            if (jo.get("proxyPolicy").getClass().toString().equals("class org.json.simple.JSONObject")) {
                jsonObject = new JSONObject();
                jsonObject = (JSONObject) jo.get("proxyPolicy");
            }
            if (jo.get("proxyPolicy").getClass().toString().equals("class java.util.LinkedHashMap")) {
                jsonObject = new JSONObject((Map) jo.get("proxyPolicy"));
            }



            ProxyPolicy proxyPolicy = new ProxyPolicy();
            proxyPolicy.setAtClass((String) jsonObject.get("@class"));
            proxyPolicy.setPattern((String) jsonObject.get("pattern"));
            service.setProxyPolicy(proxyPolicy);

        }

        if (jo.get("usernameAttributeProvider") == null) {

            UsernameAttributeProvider usernameAttributeProvider = new UsernameAttributeProvider();
            service.setUsernameAttributeProvider(usernameAttributeProvider);
        } else {

            JSONObject jsonObject = null;
            String s = jo.get("usernameAttributeProvider").getClass().toString();
            if (jo.get("usernameAttributeProvider").getClass().toString().equals("class org.json.simple.JSONObject")) {
                jsonObject = new JSONObject();
                jsonObject = (JSONObject) jo.get("usernameAttributeProvider");
            }
            if (jo.get("usernameAttributeProvider").getClass().toString().equals("class java.util.LinkedHashMap")) {
                jsonObject = new JSONObject((Map) jo.get("usernameAttributeProvider"));
            }


            UsernameAttributeProvider usernameAttributeProvider = new UsernameAttributeProvider();
            usernameAttributeProvider.setAtClass((String) jsonObject.get("@class"));
            usernameAttributeProvider.setCanonicalizationMode((String) jsonObject.get("canonicalizationMode"));
            usernameAttributeProvider.setEncryptUsername((Boolean) jsonObject.get("encryptUsername"));
            service.setUsernameAttributeProvider(usernameAttributeProvider);

        }
        if (jo.get("attributeReleasePolicy") == null) {

            AttributeReleasePolicy attributeReleasePolicy = new AttributeReleasePolicy();
            service.setAttributeReleasePolicy(attributeReleasePolicy);
        } else {
            JSONObject jsonObject = null;
            String s = jo.get("attributeReleasePolicy").getClass().toString();
            if (jo.get("attributeReleasePolicy").getClass().toString().equals("class org.json.simple.JSONObject")) {
                jsonObject = new JSONObject();
                jsonObject = (JSONObject) jo.get("attributeReleasePolicy");
            }
            if (jo.get("attributeReleasePolicy").getClass().toString().equals("class java.util.LinkedHashMap")) {
                jsonObject = new JSONObject((Map) jo.get("attributeReleasePolicy"));
            }
            AttributeReleasePolicy attributeReleasePolicy = new AttributeReleasePolicy();
            attributeReleasePolicy.setAtClass((String) jsonObject.get("@class"));
            attributeReleasePolicy.setAuthorizedToReleaseAuthenticationAttributes((boolean) jsonObject.get("authorizedToReleaseAuthenticationAttributes"));
            attributeReleasePolicy.setAuthorizedToReleaseCredentialPassword((Boolean) jsonObject.get("authorizedToReleaseCredentialPassword"));
            attributeReleasePolicy.setOrder((long) jsonObject.get("order"));
            attributeReleasePolicy.setExcludeDefaultAttributes((boolean) jsonObject.get("excludeDefaultAttributes"));
            attributeReleasePolicy.setAuthorizedToReleaseProxyGrantingTicket((boolean) jsonObject.get("authorizedToReleaseProxyGrantingTicket"));

            LinkedHashMap jsonConcentPolicy = (LinkedHashMap) jo.get("ConsentPolicy");
            if (jsonConcentPolicy==null) {
                ConsentPolicy consentPolicy = new ConsentPolicy();
                attributeReleasePolicy.setConsentPolicy(consentPolicy);
            }else{


                if (jo.get("ConsentPolicy").getClass().toString().equals("class org.json.simple.JSONObject")) {
                    jsonObject = new JSONObject();
                    jsonObject = (JSONObject) jo.get("ConsentPolicy");
                }
                if (jo.get("ConsentPolicy").getClass().toString().equals("class java.util.LinkedHashMap")) {
                    jsonObject = new JSONObject((Map) jo.get("ConsentPolicy"));
                }

                ConsentPolicy consentPolicy = new ConsentPolicy();
                consentPolicy.setAtClass((String) jsonConcentPolicy.get("@class"));
                consentPolicy.setEnabled((boolean) jsonConcentPolicy.get("enabled"));
                consentPolicy.setOrder((int) jsonConcentPolicy.get("order"));
                attributeReleasePolicy.setConsentPolicy(consentPolicy);

                JSONObject jsonPrincipalAttributesRepository = (JSONObject) jo.get("principalAttributesRepository");
                PrincipalAttributesRepository principalAttributesRepository = new PrincipalAttributesRepository();
                if (jsonPrincipalAttributesRepository == null){
                    attributeReleasePolicy.setPrincipalAttributesRepository(principalAttributesRepository);

                } else {
                    principalAttributesRepository.setAtClass((String) jsonPrincipalAttributesRepository.get("@class"));
                    principalAttributesRepository.setIgnoreResolvedAttributes((boolean) jsonPrincipalAttributesRepository.get("ignoreResolvedAttributes"));
                    principalAttributesRepository.setMergingStrategy((String) jsonPrincipalAttributesRepository.get("mergingStrategy"));
                    attributeReleasePolicy.setPrincipalAttributesRepository(principalAttributesRepository);
                    service.setAttributeReleasePolicy(attributeReleasePolicy);

                }
            }








        }

        if (jo.get("multifactorPolicy") == null) {

            MultifactorPolicy multifactorPolicy = new MultifactorPolicy();
            service.setMultifactorPolicy(multifactorPolicy);
        } else {

            JSONObject jsonObject = null ;

            if (jo.get("multifactorPolicy").getClass().toString().equals("class org.json.simple.JSONObject")) {
                jsonObject = new JSONObject();
                jsonObject = (JSONObject) jo.get("multifactorPolicy");
            }
            if (jo.get("multifactorPolicy").getClass().toString().equals("class java.util.LinkedHashMap")) {
                jsonObject = new JSONObject((Map) jo.get("multifactorPolicy"));
            }

            MultifactorPolicy multifactorPolicy = new MultifactorPolicy();
            multifactorPolicy.setFailureMode((String) jsonObject.get("failureMode"));
            multifactorPolicy.setBypassEnabled((Boolean) jsonObject.get("bypassEnabled"));
            service.setMultifactorPolicy(multifactorPolicy);



        }

        //contacts
        ArrayList arrayList = (ArrayList) jo.get("contacts");

        if (arrayList != null) {

            String temp0 = (String) arrayList.get(0);

            List contacts = new LinkedList<Contact>();

            if (arrayList!=null && arrayList != null) {
                ArrayList temp1 = (ArrayList) arrayList.get(1);
                for (int i = 0; i < temp1.size(); i++) {


                    JSONObject jsonObject = null ;



                    if ( temp1.get(i)!=null &&
                            temp1.get(i).getClass().toString().equals("class org.json.simple.JSONObject")) {
                        jsonObject = new JSONObject();
                        jsonObject = (JSONObject) temp1.get(i);
                    }
                    if (temp1.get(i)!=null &&
                            temp1.get(i).getClass().toString().equals("class java.util.LinkedHashMap")) {
                        jsonObject = new JSONObject((Map) temp1.get(i));
                    }




                    Contact contact = new Contact();
                    if (jsonObject.get("name") != null) contact.setName(jsonObject.get("name").toString());
                    Email email = new Email();
                    if (jsonObject.get("email") != null) {



                        contact.setEmail((String) jsonObject.get("email"));

                        if (jsonObject.get("phone") != (null)) contact.setPhone(jsonObject.get("phone").toString());
                        if (jsonObject.get("department") != (null))
                            contact.setDepartment(jsonObject.get("department").toString());
                        contacts.add(contact);
                    }

                }
            }
            JSONObject jsonObject = null ;


            Object[] tempObj = new Object[2];
            tempObj[0] = temp0;
            tempObj[1] = contacts;
            service.setContacts(tempObj);
        }

        JSONObject jsonObject = null ;


        if ( jo.get("multifactorPolicy")!=null &&
                jo.get("multifactorPolicy").getClass().toString().equals("class org.json.simple.JSONObject")) {
            jsonObject = new JSONObject();
            jsonObject = (JSONObject) jo.get("multifactorPolicy");
        }
        if (jo.get("multifactorPolicy")!=null &&
                jo.get("multifactorPolicy").getClass().toString().equals("class java.util.LinkedHashMap")) {
            jsonObject = new JSONObject((Map) jo.get("multifactorPolicy"));
        }

        // AccessStrategy
        jsonObject = new JSONObject((Map) jo.get("accessStrategy"));
        AccessStrategy accessStrategy = new AccessStrategy();
        if (jsonObject.get("acceptableResponseCodes") != (null))
            accessStrategy.setAcceptableResponseCodes(jsonObject.get("acceptableResponseCodes").toString());

        if (jsonObject.get("unauthorizedRedirectUrl") != (null))
            accessStrategy.setUnauthorizedRedirectUrl(jsonObject.get("unauthorizedRedirectUrl").toString());



        //accessStrategy.setAtClass(jsonObject.get("@class").toString()););
        if (jsonObject.get("endpointUrl") != null)
            accessStrategy.setEndpointUrl(jsonObject.get("endpointUrl").toString());
        if (jsonObject.get("enabled") == (null) || (boolean) jsonObject.get("enabled") == false) {

            accessStrategy.setEnabled(false);
        } else
            accessStrategy.setEnabled(true);

        if (jsonObject.get("ssoEnabled") == (null) || (boolean) jsonObject.get("ssoEnabled") == false) {

            accessStrategy.setSsoEnabled(false);
        } else
            accessStrategy.setSsoEnabled(true);


        JSONObject tempreqiredattribute = new JSONObject();
        JSONArray obj  = null;
        JSONObject t1;
        if (jsonObject.get("requiredAttributes")!=(null)) {



                Object ob1 =jsonObject.get("requiredAttributes");

                if (ob1.getClass().toString().equals("class org.json.simple.JSONArray")) {
                    obj = new JSONArray();
                    obj = (JSONArray) jsonObject.get("requiredAttributes");
                    t1 = (JSONObject) obj.get(0);
                    tempreqiredattribute = t1;
                    tempreqiredattribute.put("@class", "java.util.HashMap");

                }
                else {
                    tempreqiredattribute = new JSONObject((Map) ob1);
                    tempreqiredattribute.put("@class", "java.util.HashMap");
                }

                }

        accessStrategy.setRequiredAttributes(tempreqiredattribute);

        //Set keys = obj.keySet();

        //JSONObject ja = new JSONObject();
        //JSONObject nwq = new JSONObject();


        /*for (Object object:keys) {
            Object neobject = tempreqiredattribute.get(object.toString());

            nwq.put(object.toString(),neobject);


        }
        nwq.put("@class" , "java.util.HashMap");


        accessStrategy.setRequiredAttributes(nwq);*/





/*

        RequiredAttributes requiredAttributes = new RequiredAttributes();
        //requiredAttributes.setAtClass(tempreqiredattribute.get("@class").toString());
        ArrayList jsonArray2 = null;
        String t0 = null;
        String memberName ="member";
        if (tempreqiredattribute != null && tempreqiredattribute.get(memberName) != null) {



            ArrayList t1 = (ArrayList) tempreqiredattribute.get(memberName);


            t1 = (ArrayList) t1.get(1);
            List t1list = new LinkedList();


            if (t1!=null) {
                for (int i = 0; i < t1.size(); i++) {
                    t1list.add(t1.get(i));
                }


                Object[] members = new Object[2];

                members[0] = "java.util.HashSet";
                members[1] = t1list;
                requiredAttributes.setMember(members);
            }
        }
        accessStrategy.setRequiredAttributes(requiredAttributes);
        service.setAccessStrategy(accessStrategy);
*/

        service.setAccessStrategy(accessStrategy);


        return service;
    }

}

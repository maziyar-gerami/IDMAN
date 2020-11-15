package parsso.idman.Helpers.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import parsso.idman.Models.ServicesSubModel.*;
import parsso.idman.Models.Service;
import parsso.idman.Models.ServiceType.CasService;
import parsso.idman.Models.ServiceType.MicroService;
import parsso.idman.Repos.ServiceRepo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

@Component
public class CasServiceHelper {

    @Value("${services.folder.path}")
    String path;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ServiceRepo serviceRepo;

    private final String collection = "IDMAN_Services";


    public List<Service> listServices() throws IOException {
        File folder = new File(path); // ./services/
        String[] files = folder.list();
        JSONParser jsonParser = new JSONParser();
        List<Service> services = new LinkedList<>();
        for (String file : files) {
            if (file.endsWith(".json"))
                try {
                    Service service = analyze(file);
                    if (isCasService(service))
                        services.add(analyze(file));
                } catch (Exception e) {
                    continue;
                }
        }
        return services;
    }

    public parsso.idman.Models.ServiceType.CasService buildCasService(JSONObject jo) {

        parsso.idman.Models.ServiceType.CasService service = new parsso.idman.Models.ServiceType.CasService();
        if (jo.get("id") != null)
            service.setId(Long.valueOf(jo.get("id").toString()));

        if (jo.get("serviceId") != null) service.setServiceId(jo.get("serviceId").toString());
        if (jo.get("description") != null) service.setDescription(jo.get("description").toString());
        if (jo.get("logoutType") != null) service.setLogoutType(jo.get("logoutType").toString());
        if (jo.get("@class")!=null && isCasService(jo)) service.setAtClass(jo.get("@class").toString());
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
        } else {

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
            if (jsonConcentPolicy == null) {
                ConsentPolicy consentPolicy = new ConsentPolicy();
                attributeReleasePolicy.setConsentPolicy(consentPolicy);
            } else {

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
                if (jsonPrincipalAttributesRepository == null) {
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

            JSONObject jsonObject = null;

            if (jo.get("multifactorPolicy").getClass().toString().equals("class org.json.simple.JSONObject")) {
                jsonObject = (JSONObject) jo.get("multifactorPolicy");
            }
            if (jo.get("multifactorPolicy").getClass().toString().equals("class java.util.LinkedHashMap")) {
                jsonObject = new JSONObject((Map) jo.get("multifactorPolicy"));
            }

            MultifactorPolicy multifactorPolicy = new MultifactorPolicy();
            multifactorPolicy.setFailureMode((String) jsonObject.get("failureMode"));
            multifactorPolicy.setBypassEnabled((Boolean) jsonObject.get("bypassEnabled"));
            multifactorPolicy.setMultifactorAuthenticationProviders((ArrayList) jsonObject.get("multifactorAuthenticationProviders"));
            service.setMultifactorPolicy(multifactorPolicy);


        }

        //contacts
        ArrayList arrayList = (ArrayList) jo.get("contacts");

        if (arrayList != null) {

            String temp0 = (String) arrayList.get(0);

            List contacts = new LinkedList<Contact>();

            if (arrayList != null && arrayList != null) {
                ArrayList temp1 = (ArrayList) arrayList.get(1);
                for (int i = 0; i < temp1.size(); i++) {


                    JSONObject jsonObject = null;


                    if (temp1.get(i) != null &&
                            temp1.get(i).getClass().toString().equals("class org.json.simple.JSONObject")) {
                        jsonObject = (JSONObject) temp1.get(i);
                    }
                    if (temp1.get(i) != null &&
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


            Object[] tempObj = new Object[2];
            tempObj[0] = temp0;
            tempObj[1] = contacts;
            service.setContacts(tempObj);
        }

        JSONObject jsonObject = null;


        if (jo.get("multifactorPolicy") != null &&
                jo.get("multifactorPolicy").getClass().toString().equals("class org.json.simple.JSONObject")) {
            jsonObject = new JSONObject();
            jsonObject = (JSONObject) jo.get("multifactorPolicy");
        }
        if (jo.get("multifactorPolicy") != null &&
                jo.get("multifactorPolicy").getClass().toString().equals("class java.util.LinkedHashMap")) {
            jsonObject = new JSONObject((Map) jo.get("multifactorPolicy"));
        }

        // AccessStrategy
        jsonObject = new JSONObject((Map) jo.get("accessStrategy"));
        AccessStrategy accessStrategy = new AccessStrategy();

        AccessStrategy ac = accessStrategy.parse(jsonObject);

        service.setAccessStrategy(ac);

        return service;

    }

    boolean isCasService (JSONObject jo) {

        if (jo.get("@class").toString().toLowerCase().contains("saml"))
            return false;
        return true;

    }

    boolean isCasService (Service service) {

        if (service.getAtClass().toLowerCase().contains("saml"))
            return false;
        return true;

    }

    public CasService analyze(String file) throws IOException, ParseException {

        FileReader reader = new FileReader(path + file);
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(reader);

        reader.close();
        return buildCasService((JSONObject) obj);

    }

    public HttpStatus update(long id, JSONObject jsonObject) throws IOException, ParseException {
        Service oldService = serviceRepo.retrieveService(id);


        Service service = buildCasService(jsonObject);
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



    public HttpStatus create(JSONObject jo) {

        CasService service = buildCasService(jo);
        service.setId(new Date().getTime());
        String json = null;
        if (service!=null){

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                mongoTemplate.save(service,"Services");
                json = ow.writeValueAsString(service);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            FileWriter file = null;
            try {
                String fileName = service.getName();
                String s1 = fileName.replaceAll("\\s+", "");
                s1 = s1.replaceAll("[-,]", "");
                String filePath = s1 + "-" + service.getId();

                file = new FileWriter(path + filePath + ".json");
                file.write(json);
                file.close();
                InetAddress[] machines = null;
                if(!(service.getServiceId().contains("localhost")))
                    machines = InetAddress.getAllByName(Trim.trimServiceId(service.getServiceId()));

                List<String> IPaddresses = new LinkedList<>();

                if(machines!=null)
                for (InetAddress machine:machines)
                    IPaddresses.add(machine.getHostAddress());
                MicroService microService = new MicroService(((CasService)service).getServiceId(), IPaddresses);

                mongoTemplate.save(microService,collection);
                return HttpStatus.OK;
            } catch (IOException e) {
                e.printStackTrace();
                return HttpStatus.FORBIDDEN;
            }


        }

        return HttpStatus.OK;

    }

}

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
import parsso.idman.Helpers.UniformLogger;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceType.CasService;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Services.ServicesSubModel.*;
import parsso.idman.Repos.ServiceRepo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

@Component
public class CasServiceHelper {

    final String model = "Service";
    private final String collection = Variables.col_services;
    @Value("${services.folder.path}")
    String path;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ServiceRepo serviceRepo;

    @Autowired
    UniformLogger uniformLogger;

    public CasService buildCasService(JSONObject jo) {

        CasService service = new parsso.idman.Models.Services.ServiceType.CasService();
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


        if (jo.get("expirationPolicy") == null)
            service.setExpirationPolicy(new ExpirationPolicy());
        else {
            JSONObject jsonObject = null;
            String s = jo.get("expirationPolicy").getClass().toString();
            if (jo.get("expirationPolicy").getClass().toString().equals("class org.json.simple.JSONObject"))
                jsonObject = (JSONObject) jo.get("expirationPolicy");
            if (jo.get("expirationPolicy").getClass().toString().equals("class java.util.LinkedHashMap"))
                jsonObject = new JSONObject((Map) jo.get("expirationPolicy"));

            ExpirationPolicy expirationPolicy = new ExpirationPolicy();
            expirationPolicy.setAtClass((String) jsonObject.get("@class"));
            expirationPolicy.setDeleteWhenExpired((boolean) jsonObject.get("deleteWhenExpired"));
            expirationPolicy.setNotifyWhenDeleted((boolean) jsonObject.get("notifyWhenDeleted"));
            service.setExpirationPolicy(expirationPolicy);
        }

        if (jo.get("proxyPolicy") == null)
            service.setProxyPolicy(new ProxyPolicy());
        else {

            JSONObject jsonObject = null;
            if (jo.get("proxyPolicy").getClass().toString().equals("class org.json.simple.JSONObject"))
                jsonObject = (JSONObject) jo.get("proxyPolicy");

            if (jo.get("proxyPolicy").getClass().toString().equals("class java.util.LinkedHashMap"))
                jsonObject = new JSONObject((Map) jo.get("proxyPolicy"));

            ProxyPolicy proxyPolicy = new ProxyPolicy();
            proxyPolicy.setAtClass((String) jsonObject.get("@class"));
            proxyPolicy.setPattern((String) jsonObject.get("pattern"));
            service.setProxyPolicy(proxyPolicy);
        }

        if (jo.get("usernameAttributeProvider") == null) {
            service.setUsernameAttributeProvider(new UsernameAttributeProvider());
        } else {

            JSONObject jsonObject = null;
            if (jo.get("usernameAttributeProvider").getClass().toString().equals("class org.json.simple.JSONObject"))
                jsonObject = (JSONObject) jo.get("usernameAttributeProvider");

            if (jo.get("usernameAttributeProvider").getClass().toString().equals("class java.util.LinkedHashMap"))
                jsonObject = new JSONObject((Map) jo.get("usernameAttributeProvider"));

            UsernameAttributeProvider usernameAttributeProvider = new UsernameAttributeProvider();
            usernameAttributeProvider.setAtClass((String) jsonObject.get("@class"));
            usernameAttributeProvider.setCanonicalizationMode((String) jsonObject.get("canonicalizationMode"));
            usernameAttributeProvider.setEncryptUsername((Boolean) jsonObject.get("encryptUsername"));
            service.setUsernameAttributeProvider(usernameAttributeProvider);
        }
        if (jo.get("attributeReleasePolicy") == null && jo.get("accessStrategy") != null) {

            AttributeReleasePolicy attributeReleasePolicy = new AttributeReleasePolicy();
            service.setAttributeReleasePolicy(attributeReleasePolicy);
        } else {
            JSONObject jsonObject = null;
            String s = jo.get("attributeReleasePolicy").getClass().toString();
            if (jo.get("attributeReleasePolicy").getClass().toString().equals("class org.json.simple.JSONObject"))
                jsonObject = (JSONObject) jo.get("attributeReleasePolicy");

            if (jo.get("attributeReleasePolicy").getClass().toString().equals("class java.util.LinkedHashMap"))
                jsonObject = new JSONObject((Map) jo.get("attributeReleasePolicy"));

            AttributeReleasePolicy attributeReleasePolicy = new AttributeReleasePolicy();
            attributeReleasePolicy.setAtClass((String) jsonObject.get("@class"));
            attributeReleasePolicy.setAuthorizedToReleaseAuthenticationAttributes((boolean) jsonObject.get("authorizedToReleaseAuthenticationAttributes"));
            attributeReleasePolicy.setAuthorizedToReleaseCredentialPassword((Boolean) jsonObject.get("authorizedToReleaseCredentialPassword"));
            try {
                attributeReleasePolicy.setOrder((int) jsonObject.get("order"));
            } catch (Exception e) {
                attributeReleasePolicy.setOrder((long) jsonObject.get("order"));
            }
            attributeReleasePolicy.setExcludeDefaultAttributes((boolean) jsonObject.get("excludeDefaultAttributes"));
            attributeReleasePolicy.setAuthorizedToReleaseProxyGrantingTicket((boolean) jsonObject.get("authorizedToReleaseProxyGrantingTicket"));

            LinkedHashMap jsonConcentPolicy = (LinkedHashMap) jo.get("ConsentPolicy");
            if (jsonConcentPolicy == null) {
                ConsentPolicy consentPolicy = new ConsentPolicy();
                attributeReleasePolicy.setConsentPolicy(consentPolicy);
            } else {

                if (jo.get("ConsentPolicy").getClass().toString().equals("class org.json.simple.JSONObject"))
                    jsonObject = (JSONObject) jo.get("ConsentPolicy");

                if (jo.get("ConsentPolicy").getClass().toString().equals("class java.util.LinkedHashMap"))
                    jsonObject = new JSONObject((Map) jo.get("ConsentPolicy"));

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

        if (jo.get("multifactorPolicy") != null) {

            JSONObject jsonObject = null;

            if (jo.get("multifactorPolicy").getClass().toString().equals("class org.json.simple.JSONObject"))
                jsonObject = (JSONObject) jo.get("multifactorPolicy");

            else if (jo.get("multifactorPolicy").getClass().toString().equals("class java.util.LinkedHashMap"))
                jsonObject = new JSONObject((Map) jo.get("multifactorPolicy"));

            if (jsonObject.get("multifactorAuthenticationProviders") != null) {

                MultifactorPolicy multifactorPolicy = new MultifactorPolicy();

                if (jsonObject.get("failureMode") != null)
                    multifactorPolicy.setFailureMode((String) jsonObject.get("failureMode"));
                if (jsonObject.get("bypassEnabled") != null)
                    multifactorPolicy.setBypassEnabled((Boolean) jsonObject.get("bypassEnabled"));
                if (jsonObject.get("multifactorAuthenticationProviders") != null)
                    multifactorPolicy.setMultifactorAuthenticationProviders(jsonObject.get("multifactorAuthenticationProviders").toString());
                service.setMultifactorPolicy(multifactorPolicy);

            }
        }

        // AccessStrategy
        JSONObject jsonObject;
        if (jo.get("accessStrategy") != null) {
            jsonObject = new JSONObject((Map) jo.get("accessStrategy"));
            AccessStrategy accessStrategy = new AccessStrategy();

            AccessStrategy ac = accessStrategy.parse(jsonObject);

            service.setAccessStrategy(ac);
        }

        //contacts
        if (jo.get("contacts") != null) {

            ArrayList arrayList = (ArrayList) jo.get("contacts");

            if (arrayList != null) {

                String temp0 = (String) arrayList.get(0);

                List contacts = new LinkedList<Contact>();

                if (arrayList != null && arrayList != null) {
                    ArrayList temp1 = (ArrayList) arrayList.get(1);
                    for (int i = 0; i < temp1.size(); i++) {

                        JSONObject jsonObject1 = null;

                        if (temp1.get(i) != null &&
                                temp1.get(i).getClass().toString().equals("class org.json.simple.JSONObject")) {
                            jsonObject1 = (JSONObject) temp1.get(i);
                        }
                        if (temp1.get(i) != null &&
                                temp1.get(i).getClass().toString().equals("class java.util.LinkedHashMap")) {
                            jsonObject1 = new JSONObject((Map) temp1.get(i));
                        }


                        Contact contact = new Contact();
                        if (jsonObject1.get("name") != null) contact.setName(jsonObject1.get("name").toString());
                        if (jsonObject1.get("email") != null) {
                            contact.setEmail((String) jsonObject1.get("email"));

                            if (jsonObject1.get("phone") != (null))
                                contact.setPhone(jsonObject1.get("phone").toString());
                            if (jsonObject1.get("department") != (null))
                                contact.setDepartment(jsonObject1.get("department").toString());
                            contacts.add(contact);
                        }

                    }
                }

                Object[] tempObj = new Object[2];
                tempObj[0] = temp0;
                tempObj[1] = contacts;
                service.setContacts(tempObj);
            }
        }

        return service;

    }

    public CasService analyze(String file) throws IOException, ParseException {

        FileReader reader = new FileReader(path + file);
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(reader);

        reader.close();
        return buildCasService((JSONObject) obj);

    }

    public HttpStatus update(String doerID, long id, JSONObject jsonObject) throws IOException, ParseException {
        Service oldService = serviceRepo.retrieveService(id);

        Service service = buildCasService(jsonObject);
        service.setId(id);

        String json = null;

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            json = ow.writeValueAsString(service);
        } catch (JsonProcessingException e) {
            uniformLogger.record(doerID, new ReportMessage(model, String.valueOf(id), "",
                    Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Opening file"));
            return HttpStatus.FORBIDDEN;
        }

        FileWriter file;
        try {

            File oldFile = new File(path + oldService.getName() + "-" + service.getId() + ".json");
            oldFile.delete();
            String fileName = service.getName();
            String s1 = fileName.replaceAll("\\s+", "");
            s1 = s1.replaceAll("[-,]", "");
            String filePath = s1 + "-" + service.getId();

            file = new FileWriter(path + filePath + ".json");
            file.write(json);
            file.close();
            uniformLogger.record(doerID, new ReportMessage(model, String.valueOf(id), "",
                    Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ""));
            return HttpStatus.OK;
        } catch (IOException e) {
            uniformLogger.record(doerID, new ReportMessage(model, String.valueOf(id), "",
                    Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Writing file"));
            return HttpStatus.FORBIDDEN;
        }

    }

    public long create(String doerID, JSONObject jo) {

        CasService service = buildCasService(jo);
        service.setId(new Date().getTime());
        String json = null;
        if (service != null) {

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                mongoTemplate.save(service, collection);
                json = ow.writeValueAsString(service);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            FileWriter file;
            try {
                String fileName = service.getName();
                String s1 = fileName.replaceAll("\\s+", "");
                s1 = s1.replaceAll("[-,]", "");
                String filePath = s1 + "-" + service.getId();

                InetAddress[] machines = null;
                if (!(service.getServiceId().contains("localhost")))
                    try {
                        machines = InetAddress.getAllByName(Trim.trimServiceId(service.getServiceId()));
                    } catch (Exception e) {
                        uniformLogger.record("System", new ReportMessage(Variables.MODEL_SERVICE, service.getServiceId(),
                                "IP", Variables.ACTION_GET, Variables.RESULT_FAILED, ""));
                        machines = null;
                    }

                List<String> IPaddresses = new LinkedList<>();

                if (machines != null)
                    for (InetAddress machine : machines)
                        IPaddresses.add(machine.getHostAddress());
                MicroService microService = new MicroService((service).getServiceId(), IPaddresses);

                file = new FileWriter(path + filePath + ".json");
                file.write(json);
                file.close();

                mongoTemplate.save(microService, collection);
                uniformLogger.record(doerID, new ReportMessage(Variables.MODEL_SERVICE, service.getServiceId(),
                        "", Variables.ACTION_CREATE, Variables.RESULT_SUCCESS, ""));

                return service.getId();
            } catch (IOException e) {

                uniformLogger.record(doerID, new ReportMessage(Variables.MODEL_SERVICE, service.getServiceId(),
                        "", Variables.ACTION_CREATE, Variables.RESULT_FAILED, ""));
                return 0;
            }

        }

        return service.getId();

    }

}

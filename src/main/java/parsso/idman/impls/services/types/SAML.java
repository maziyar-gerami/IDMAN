package parsso.idman.impls.services.types;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import parsso.idman.helpers.Comparison;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.service.Trim;
import parsso.idman.impls.services.RetrieveService;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.services.Service;
import parsso.idman.models.services.serviceType.SamlService;
import parsso.idman.models.services.servicesSubModel.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SAML {
  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;

  public SAML(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public SamlService buildSamlService(JSONObject jo) {

    SamlService service = new SamlService();
    if (jo.get("id") != null)
      service.setId(Long.parseLong(jo.get("id").toString()));

    if (jo.get("serviceId") != null)
      service.setServiceId(jo.get("serviceId").toString());
    if (jo.get("@class") != null)
      service.setAtClass(jo.get("@class").toString());
    if (jo.get("name") != null)
      service.setName(jo.get("name").toString());
    if (jo.get("id") != null)
      service.setId(Long.parseLong(jo.get("id").toString()));
    if (jo.get("evaluationOrder") != null)
      service.setEvaluationOrder(Integer.parseInt(jo.get("evaluationOrder").toString()));
    if (jo.get("metadataLocation") != null)
      service.setMetadataLocation(jo.get("metadataLocation").toString());
    if (jo.get("logoutType") != null)
      service.setLogoutType(jo.get("logoutType").toString());
    if (jo.get("logoutUrl") != null)
      service.setLogoutUrl(jo.get("logoutUrl").toString());
    if (jo.get("privacyUrl") != null)
      service.setPrivacyUrl(jo.get("privacyUrl").toString());
    if (jo.get("logo") != null)
      service.setLogo(jo.get("logo").toString());
    if (jo.get("informationUrl") != null)
      service.setInformationUrl(jo.get("informationUrl").toString());
    if (jo.get("description") != null)
      service.setDescription(jo.get("description").toString());
    if (jo.get("attributeReleasePolicy") == null)
      service.setAttributeReleasePolicy(new AttributeReleasePolicy());
    else {
      JSONObject jsonObject = null;
      if (jo.get("attributeReleasePolicy").getClass().toString().equals("class org.json.simple.JSONObject"))
        jsonObject = (JSONObject) jo.get("attributeReleasePolicy");

      if (jo.get("attributeReleasePolicy").getClass().toString().equals("class java.util.LinkedHashMap"))
        jsonObject = new JSONObject((Map) jo.get("attributeReleasePolicy"));

      AttributeReleasePolicy attributeReleasePolicy = new AttributeReleasePolicy();
      attributeReleasePolicy.setAtClass((String) Objects.requireNonNull(jsonObject).get("@class"));
      attributeReleasePolicy.setAuthorizedToReleaseAuthenticationAttributes(
          (boolean) jsonObject.get("authorizedToReleaseAuthenticationAttributes"));
      attributeReleasePolicy.setAuthorizedToReleaseCredentialPassword(
          (Boolean) jsonObject.get("authorizedToReleaseCredentialPassword"));
      try {
        attributeReleasePolicy.setOrder((int) jsonObject.get("order"));
      } catch (Exception e) {
        attributeReleasePolicy.setOrder((long) jsonObject.get("order"));
      }
      attributeReleasePolicy.setExcludeDefaultAttributes((boolean) jsonObject.get("excludeDefaultAttributes"));
      attributeReleasePolicy.setAuthorizedToReleaseProxyGrantingTicket(
          (boolean) jsonObject.get("authorizedToReleaseProxyGrantingTicket"));

      LinkedHashMap jsonConcentPolicy = (LinkedHashMap) jo.get("ConsentPolicy");
      if (jsonConcentPolicy == null) {
        ConsentPolicy consentPolicy = new ConsentPolicy();
        attributeReleasePolicy.setConsentPolicy(consentPolicy);
      } else {

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
          principalAttributesRepository.setIgnoreResolvedAttributes(
              (boolean) jsonPrincipalAttributesRepository.get("ignoreResolvedAttributes"));
          principalAttributesRepository
              .setMergingStrategy((String) jsonPrincipalAttributesRepository.get("mergingStrategy"));
          attributeReleasePolicy.setPrincipalAttributesRepository(principalAttributesRepository);
          service.setAttributeReleasePolicy(attributeReleasePolicy);

        }
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

    if (jo.get("multifactorPolicy") != null) {

      jsonObject = null;

      if (jo.get("multifactorPolicy").getClass().toString().equals("class org.json.simple.JSONObject"))
        jsonObject = (JSONObject) jo.get("multifactorPolicy");

      else if (jo.get("multifactorPolicy").getClass().toString().equals("class java.util.LinkedHashMap"))
        jsonObject = new JSONObject((Map) jo.get("multifactorPolicy"));

      if (Objects.requireNonNull(jsonObject).get("multifactorAuthenticationProviders") != null) {

        MultifactorPolicy multifactorPolicy = new MultifactorPolicy();

        if (jsonObject.get("failureMode") != null)
          multifactorPolicy.setFailureMode((String) jsonObject.get("failureMode"));
        if (jsonObject.get("bypassEnabled") != null)
          multifactorPolicy.setBypassEnabled((Boolean) jsonObject.get("bypassEnabled"));
        if (jsonObject.get("multifactorAuthenticationProviders") != null)
          multifactorPolicy.setMultifactorAuthenticationProviders(
              jsonObject.get("multifactorAuthenticationProviders").toString());
        service.setMultifactorPolicy(multifactorPolicy);
      }
    }

    // contacts
    if (jo.get("contacts") != null) {

      ArrayList<Object> arrayList = (ArrayList) jo.get("contacts");

      if (arrayList != null) {

        String temp0 = (String) arrayList.get(0);

        List<Contact> contacts = new LinkedList<>();

        ArrayList<Object> temp1 = (ArrayList) arrayList.get(1);
        for (Object o : temp1) {

          JSONObject jsonObject1 = null;

          if (o != null &&
              o.getClass().toString().equals("class org.json.simple.JSONObject"))
            jsonObject1 = (JSONObject) o;

          if (o != null &&
              o.getClass().toString().equals("class java.util.LinkedHashMap"))
            jsonObject1 = new JSONObject((Map) o);

          Contact contact = new Contact();
          if (Objects.requireNonNull(jsonObject1).get("name") != null)
            contact.setName(jsonObject1.get("name").toString());

          if (jsonObject1.get("email") != null) {
            contact.setEmail((String) jsonObject1.get("email"));

            if (jsonObject1.get("phone") != (null))
              contact.setPhone(jsonObject1.get("phone").toString());
            if (jsonObject1.get("department") != (null))
              contact.setDepartment(jsonObject1.get("department").toString());
            contacts.add(contact);
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

  public long create(String doerID, JSONObject jo) {

    SamlService service = buildSamlService(jo);
    service.setId(new Date().getTime());
    String json = null;

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    try {
      String collection = Variables.col_services;
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
          e.printStackTrace();
          uniformLogger.warn(Variables.DOER_SYSTEM,
              new ReportMessage(Variables.MODEL_SERVICE, service.getId(),
                  "IP", Variables.ACTION_GET, Variables.RESULT_FAILED, ""));
          machines = null;
        }

      List<String> IPaddresses = new LinkedList<>();

      if (machines != null)
        for (InetAddress machine : machines)
          IPaddresses.add(machine.getHostAddress());

      file = new FileWriter(new Settings(mongoTemplate).retrieve(Variables.SERVICE_FOLDER_PATH).getValue()
          + filePath + ".json");
      file.write(Objects.requireNonNull(json));
      file.close();

      uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_SERVICE, service.getId(),
          "", Variables.ACTION_CREATE, Variables.RESULT_SUCCESS,
          new Comparison().compare(null, service.getAccessStrategy()), ""));
      return service.getId();
    } catch (IOException e) {
      e.printStackTrace();
      uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, service.getId(),
          "", Variables.ACTION_CREATE, Variables.RESULT_FAILED, ""));
      return 0;
    }

  }

  public HttpStatus update(String doerID, long id, JSONObject jsonObject) {
    Service oldService = new RetrieveService(mongoTemplate).retrieveService(id);

    Service service = buildSamlService(jsonObject);
    service.setId(id);

    String json = null;

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    try {
      json = ow.writeValueAsString(service);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, id, "",
          Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Fetching"));
    }

    FileWriter file;
    try {

      File oldFile = new File(new Settings(mongoTemplate).retrieve(Variables.SERVICE_FOLDER_PATH).getValue()
          + oldService.getName() + "-" + service.getId() + ".json");
      oldFile.delete();
      String fileName = service.getName();
      String s1 = fileName.replaceAll("\\s+", "");
      s1 = s1.replaceAll("[-,]", "");
      String filePath = s1 + "-" + service.getId();

      file = new FileWriter(new Settings(mongoTemplate).retrieve(Variables.SERVICE_FOLDER_PATH).getValue()
          + filePath + ".json");
      file.write(Objects.requireNonNull(json));
      file.close();
      uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_SERVICE, id, "", Variables.ACTION_UPDATE,
          Variables.RESULT_SUCCESS, oldService, service, ""));
      return HttpStatus.OK;
    } catch (IOException e) {
      e.printStackTrace();
      uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, id, "", Variables.ACTION_UPDATE,
          Variables.RESULT_FAILED, oldService, service, "Saving file"));
      return HttpStatus.FORBIDDEN;
    }

  }

  public SamlService analyze(String file) throws IOException, ParseException {

    FileReader reader = new FileReader(
        new Settings(mongoTemplate).retrieve(Variables.SERVICE_FOLDER_PATH).getValue() + file);
    JSONParser jsonParser = new JSONParser();
    Object obj = jsonParser.parse(reader);

    reader.close();
    return buildSamlService((JSONObject) obj);
  }
}

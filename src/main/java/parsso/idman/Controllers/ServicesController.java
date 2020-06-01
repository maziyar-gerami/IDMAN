package parsso.idman.Controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Controller
public class ServicesController {

    @GetMapping("/services")
    public String Services() {
        return "services";
    }

    @GetMapping("/createservice")
    public String CreateService() {
        return "createservice";
    }

    @PostMapping("/createservice")
    public void AddService(@RequestParam Map<String, String> allParams) {
        JSONObject jsonObject = new JSONObject();
        File service;
        String class1 = "org.apereo.cas.services.RegexRegisteredService";
        int id = Integer.parseInt(LocalTime.now().toString().replace(":", "").replace(".", ""));
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
            return;
        }
        if(!allParams.get("serviceId").equals("")){
            jsonObject.put("serviceId", "^" + allParams.get("serviceId") + ".+");
        }else{
            return;
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
            return;
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
                return;
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
            return;
        }
        if(!allParams.get("email").equals("")){
            contactsObject.put("email", allParams.get("email"));
        }else{
            return;
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
            return;
        }
        if(!allParams.get("logoutType").equals("")){
            jsonObject.put("logoutType", allParams.get("logoutType"));
        }else{
            return;
        }


        try {
            FileWriter file = new FileWriter("/etc/cas/services/" + allParams.get("name") + "-" + id + ".json");
            file.write(jsonObject.toJSONString().replace("\\/", "/"));
            file.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
    }
}

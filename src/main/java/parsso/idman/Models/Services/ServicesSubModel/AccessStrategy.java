package parsso.idman.Models.Services.ServicesSubModel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import parsso.idman.Utils.Convertor.DateConverter;

import java.time.ZoneId;
import java.util.Map;

@Setter
@Getter

public class AccessStrategy {

    @JsonIgnore
    ZoneId zoneId = ZoneId.of("UTC+03:30");
    @JsonProperty("@class")
    private String atClass;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean enabled;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int order;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean ssoEnabled;
    private DelegatedAuthenticationPolicy delegatedAuthenticationPolicy;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private JSONObject requiredAttributes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean requireAllAttributes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String unauthorizedRedirectUrl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean caseInsensitive;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private JSONObject rejectedAttributes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String startingDateTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String endingDateTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String endpointUrl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String acceptableResponseCodes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String groupField;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ProxyPolicy proxyPolicy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String groovyScript;

    public AccessStrategy() {
        if (startingDateTime != null || startingDateTime != "" ||
                endingDateTime != null || endingDateTime != "")
            atClass = "org.apereo.cas.services.TimeBasedRegisteredServiceAccessStrategy";
        else
            atClass = "org.apereo.cas.services.DefaultRegisteredServiceAccessStrategy";
        enabled = true;
        ssoEnabled = true;
        requireAllAttributes = false;
        caseInsensitive = false;
        rejectedAttributes = new JSONObject();
        order = 0;
        delegatedAuthenticationPolicy = new DelegatedAuthenticationPolicy();
        requiredAttributes = new JSONObject();

    }
    //private List<Contact> contacts;

    public void setStartingDateTimeForPost(String startingDateTime) {
        this.startingDateTime = seTime(startingDateTime);
    }

    public void setEndingDateTimeForPost(String endingDateTime) {
        this.endingDateTime = seTime(endingDateTime);

    }

    public void setStartingDateTimeForGet(String startingDateTime) {
        this.startingDateTime = startingDateTime;
    }

    public void setEndingDateTimeForGet(String endingDateTime) {
        this.endingDateTime = endingDateTime;

    }

    public String seTime(String seTime) {

        String year = seTime.substring(0, 4);
        String month = seTime.substring(5, 7);
        String day = seTime.substring(8, 10);

        String hours = seTime.substring(11, 13);
        String minutes = seTime.substring(14, 16);
        String seconds = seTime.substring(17, 19);

        String milliSeconds = seTime.substring(20, 23);

        String tf = seTime.substring(23);

        return (convertDate(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day))) +
        'T' + hours + ':' + minutes + ':' + seconds + '.' + milliSeconds + tf;

    }

    String convertDate(int Y, int M, int D) {

        DateConverter dateConverter = new DateConverter();
        dateConverter.persianToGregorian(Y, M, D);

        return dateConverter.getYear() + "-" + String.format("%02d", dateConverter.getMonth()) + "-" + String.format("%02d", dateConverter.getDay());
    }

    public AccessStrategy parse(JSONObject jsonObject) {

        AccessStrategy accessStrategy = new AccessStrategy();


        if (jsonObject.get("unauthorizedRedirectUrl") != (null))
            accessStrategy.setUnauthorizedRedirectUrl(jsonObject.get("unauthorizedRedirectUrl").toString());


        if (jsonObject.get("startingDateTime") != null) {
            String s = jsonObject.get("startingDateTime").toString();

            if (Integer.valueOf(s.substring(0, 4)) > 2000)
                accessStrategy.setStartingDateTimeForGet(jsonObject.get("startingDateTime").toString());
            if (Integer.valueOf(s.substring(0, 4)) < 2000)
                accessStrategy.setStartingDateTimeForPost(jsonObject.get("startingDateTime").toString());
        }

        if (jsonObject.get("endingDateTime") != null) {
            String s = jsonObject.get("endingDateTime").toString();

            accessStrategy.setEndingDateTimeForGet(jsonObject.get("endingDateTime").toString());

            if (Integer.valueOf(s.substring(0, 4)) > 2000)
                accessStrategy.setEndingDateTimeForGet(jsonObject.get("endingDateTime").toString());
            if (Integer.valueOf(s.substring(0, 4)) < 2000)
                accessStrategy.setEndingDateTimeForPost(jsonObject.get("endingDateTime").toString());

        }

        if (jsonObject.get("endpointUrl") != null && jsonObject.get("acceptableResponseCodes") != (null) ) {

            accessStrategy.setAtClass("org.apereo.cas.services.RemoteEndpointServiceAccessStrategy");

            accessStrategy.setEndpointUrl(jsonObject.get("endpointUrl").toString());

            accessStrategy.setAcceptableResponseCodes(jsonObject.get("acceptableResponseCodes").toString());
        }

        accessStrategy.setEnabled(jsonObject.get("enabled") != (null) && (boolean) jsonObject.get("enabled") != false);

        accessStrategy.setSsoEnabled(jsonObject.get("ssoEnabled") != (null) && (boolean) jsonObject.get("ssoEnabled") != false);


        JSONObject tempReqiredAttribute = new JSONObject();
        JSONArray obj = null;
        JSONObject t1;
        if (jsonObject.get("requiredAttributes") != (null)) {

            Object ob1 = jsonObject.get("requiredAttributes");

            if (ob1.getClass().toString().equals("class org.json.simple.JSONArray")) {
                obj = new JSONArray();
                obj = (JSONArray) jsonObject.get("requiredAttributes");
                t1 = (JSONObject) obj.get(0);
                tempReqiredAttribute = t1;
                tempReqiredAttribute.put("@class", "java.util.HashMap");

            } else {
                tempReqiredAttribute = new JSONObject((Map) ob1);
                tempReqiredAttribute.put("@class", "java.util.HashMap");
            }

        }

        accessStrategy.setRequiredAttributes(tempReqiredAttribute);


        JSONObject tempRejectedAttribute = new JSONObject();

        if (jsonObject.get("rejectedAttributes") != (null)) {

            Object ob1 = jsonObject.get("rejectedAttributes");

            if (ob1.getClass().toString().equals("class org.json.simple.JSONArray")) {
                obj = new JSONArray();
                obj = (JSONArray) jsonObject.get("rejectedAttributes");
                t1 = (JSONObject) obj.get(0);
                tempRejectedAttribute = t1;
                tempRejectedAttribute.put("@class", "java.util.HashMap");

            } else {
                tempRejectedAttribute = new JSONObject((Map) ob1);
                tempRejectedAttribute.put("@class", "java.util.HashMap");
            }

        }

        accessStrategy.setRejectedAttributes(tempRejectedAttribute);

        return accessStrategy;

    }


}

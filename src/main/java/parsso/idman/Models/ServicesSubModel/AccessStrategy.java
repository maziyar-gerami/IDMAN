package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import parsso.idman.utils.Convertor.DateConverter;

@Setter
@Getter

public class AccessStrategy {

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
        if (startingDateTime != null)
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

        String miliSeconds = seTime.substring(20, 23);
        String tf1 = seTime.substring(24, 25);
        String tf2 = seTime.substring(26, 28);


        return (convertDate(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day))) +
                'T' + hours + ':' + minutes + ':' + seconds + '.' + miliSeconds + seTime.charAt(23) + tf1 + ":" + tf2;


    }

    String convertDate(int Y, int M, int D) {

        DateConverter dateConverter = new DateConverter();
        dateConverter.persianToGregorian(Y, M, D);

        return dateConverter.getYear() + "-" + String.format("%02d", dateConverter.getMonth()) + "-" + String.format("%02d", dateConverter.getDay());
    }

}

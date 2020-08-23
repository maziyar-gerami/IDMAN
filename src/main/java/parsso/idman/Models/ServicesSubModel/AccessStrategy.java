package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class AccessStrategy {

    public AccessStrategy(){
        atClass = "org.apereo.cas.services.RemoteEndpointServiceAccessStrategy";
        enabled = true;
        ssoEnabled = true;
        requireAllAttributes = true;
        caseInsensitive = false;
        rejectedAttributes = new RejectedAttributes();
        order = 0;
        delegatedAuthenticationPolicy = new DelegatedAuthenticationPolicy();
    }
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
    private RequiredAttributes requiredAttributes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean requireAllAttributes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String unauthorizedRedirectUrl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean caseInsensitive;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private RejectedAttributes rejectedAttributes;
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
    //private List<Contact> contacts;

}

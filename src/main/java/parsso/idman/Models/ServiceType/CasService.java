package parsso.idman.Models.ServiceType;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.ServicesSubModel.*;
import parsso.idman.Models.Service;

import java.util.LinkedList;

@Setter
@Getter

public class CasService extends Service {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ExpirationPolicy expirationPolicy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String informationUrl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UsernameAttributeProvider usernameAttributeProvider;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String privacyUrl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String logo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String theme;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ProxyPolicy proxyPolicy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object[] requiredHandlers;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object[] environments;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String logoutType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String publicKey;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String logoutUrl;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Property properties;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object[] contacts;
    public CasService() {

        super.setAtClass("org.apereo.cas.services.RegexRegisteredService");
        properties = new Property();
        super.setEvaluationOrder(1);

        requiredHandlers = new Object[2];
        requiredHandlers[0] = "java.util.HashSet";
        requiredHandlers[1] = new LinkedList<>();

        environments = new Object[2];
        environments[0] = "java.util.HashSet";
        environments[1] = new LinkedList<>();

        contacts = new Object[2];
        contacts[0] = "java.util.ArrayList";
        contacts[1] = new LinkedList<>();

    }
}


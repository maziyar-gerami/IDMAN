package parsso.idman.Models.ServiceType;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.Service;
import parsso.idman.Models.ServicesSubModel.ExpirationPolicy;
import parsso.idman.Models.ServicesSubModel.Property;
import parsso.idman.Models.ServicesSubModel.ProxyPolicy;
import parsso.idman.Models.ServicesSubModel.UsernameAttributeProvider;

import java.util.LinkedList;

@Setter
@Getter

public class CasService extends Service {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ExpirationPolicy expirationPolicy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UsernameAttributeProvider usernameAttributeProvider;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String theme;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ProxyPolicy proxyPolicy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object[] requiredHandlers;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object[] environments;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String publicKey;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Property properties;

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

        Object[] contacts = new Object[2];
        contacts[0] = "java.util.ArrayList";
        contacts[1] = new LinkedList<>();
        super.setContacts(contacts);

    }
}


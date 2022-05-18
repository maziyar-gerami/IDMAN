package parsso.idman.models.services.servicesSubModel;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

@Setter
@Getter

public class DelegatedAuthenticationPolicy {
    @JsonProperty("@class")
    String atClass;
    Object[] allowedProviders;
    boolean permitUndefined;
    boolean exclusive;

    public DelegatedAuthenticationPolicy() {
        atClass = "org.apereo.cas.services.DefaultRegisteredServiceDelegatedAuthenticationPolicy";
        exclusive = false;
        permitUndefined = true;
        allowedProviders = new Object[2];
        allowedProviders[0] = "java.util.ArrayList";
        allowedProviders[1] = new LinkedList<>();


    }
}

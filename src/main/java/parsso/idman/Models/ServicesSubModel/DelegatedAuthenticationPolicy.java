package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

@Setter
@Getter

public class DelegatedAuthenticationPolicy {

    public DelegatedAuthenticationPolicy(){
        atClass = "org.apereo.cas.services.DefaultRegisteredServiceDelegatedAuthenticationPolicy";
        exclusive = false;
        permitUndefined = true;
        allowedProviders = new Object[2];
        allowedProviders[0] = "java.util.ArrayList";
        allowedProviders[1] = new LinkedList<>();



    }

    @JsonProperty("@class")
    String atClass;
    Object[] allowedProviders;

    boolean permitUndefined;
    boolean exclusive;
}
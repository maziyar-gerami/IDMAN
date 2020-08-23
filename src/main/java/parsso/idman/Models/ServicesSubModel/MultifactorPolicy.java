package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

@Setter
@Getter

public class MultifactorPolicy {

    public  MultifactorPolicy(){
        atClass = "org.apereo.cas.services.DefaultRegisteredServiceMultifactorPolicy";
        multifactorAuthenticationProviders = new Object[2];
        failureMode ="UNDEFINED";
        bypassEnabled = false;
        multifactorAuthenticationProviders[0] =  "java.util.HashSet";
        multifactorAuthenticationProviders[1] =  new LinkedList<>();

    }

    @JsonProperty("@class")
    private String atClass;
    Object[] multifactorAuthenticationProviders;
    String failureMode;
    Boolean bypassEnabled;

}

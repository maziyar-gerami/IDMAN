package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;

@Setter
@Getter

public class MultifactorPolicy {

    public  MultifactorPolicy(){
        atClass = "org.apereo.cas.services.DefaultRegisteredServiceMultifactorPolicy";
        failureMode ="UNDEFINED";
        bypassEnabled = false;
    }

    @JsonProperty("@class")
    private String atClass;
    ArrayList multifactorAuthenticationProviders;
    String failureMode;
    Boolean bypassEnabled;

}

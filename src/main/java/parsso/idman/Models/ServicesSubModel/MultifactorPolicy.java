package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter

public class MultifactorPolicy {

    String multifactorAuthenticationProviders;
    String failureMode;
    Boolean bypassEnabled;
    @JsonProperty("@class")
    private String atClass;
    public MultifactorPolicy() {
        multifactorAuthenticationProviders = null;
        atClass = "org.apereo.cas.services.DefaultRegisteredServiceMultifactorPolicy";
        failureMode = "UNDEFINED";
        bypassEnabled = false;
    }

}

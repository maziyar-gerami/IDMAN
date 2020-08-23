package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsentPolicy {

    public ConsentPolicy(){
        atClass = "org.apereo.cas.services.consent.DefaultRegisteredServiceConsentPolicy";
        enabled = true;
        order=0;
    }

    @JsonProperty("@class")
    String atClass;
    boolean enabled;
    int order;


}

package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UsernameAttributeProvider {

    public UsernameAttributeProvider(){
        atClass = "org.apereo.cas.services.DefaultRegisteredServiceUsernameProvider";
        canonicalizationMode = "NONE";
        encryptUsername = false;

    }
    @JsonProperty("@class")
    private String atClass;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private  String canonicalizationMode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean encryptUsername;
}

package parsso.idman.Models.ServicesSubModel;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PrincipalAttributesRepository {

    @JsonProperty("@class")
    private String atClass;
    private String mergingStrategy;
    private boolean ignoreResolvedAttributes;

    public PrincipalAttributesRepository() {
        atClass = "org.apereo.cas.authentication.principal.DefaultPrincipalAttributesRepository";
        mergingStrategy = "MULTIVALUED";
        ignoreResolvedAttributes = false;


    }


}

package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

@Setter
@Getter

public class AttributeReleasePolicy {

    public AttributeReleasePolicy() {
        atClass = "org.apereo.cas.services.ReturnAllAttributeReleasePolicy";
        authorizedToReleaseCredentialPassword = false;
        authorizedToReleaseProxyGrantingTicket = false;
        excludeDefaultAttributes = false;
        authorizedToReleaseAuthenticationAttributes = false;
        consentPolicy = new ConsentPolicy();
        order=0;
        principalAttributesRepository = new PrincipalAttributesRepository();

    }
    @JsonProperty("@class")
    String atClass;
    boolean authorizedToReleaseCredentialPassword;
    boolean authorizedToReleaseProxyGrantingTicket;
    boolean excludeDefaultAttributes;
    boolean authorizedToReleaseAuthenticationAttributes;
    long order;
    ConsentPolicy consentPolicy;
    PrincipalAttributesRepository principalAttributesRepository;

}

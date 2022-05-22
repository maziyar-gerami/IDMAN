package parsso.idman.models.services.servicesSubModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class AttributeReleasePolicy {
  @JsonProperty("@class")
  String atClass;
  boolean authorizedToReleaseCredentialPassword;
  boolean authorizedToReleaseProxyGrantingTicket;
  boolean excludeDefaultAttributes;
  boolean authorizedToReleaseAuthenticationAttributes;
  long order;
  ConsentPolicy consentPolicy;
  PrincipalAttributesRepository principalAttributesRepository;

  public AttributeReleasePolicy() {
    atClass = "org.apereo.cas.services.ReturnAllAttributeReleasePolicy";
    authorizedToReleaseCredentialPassword = false;
    authorizedToReleaseProxyGrantingTicket = false;
    excludeDefaultAttributes = false;
    authorizedToReleaseAuthenticationAttributes = false;
    consentPolicy = new ConsentPolicy();
    order = 0;
    principalAttributesRepository = new PrincipalAttributesRepository();

  }

}

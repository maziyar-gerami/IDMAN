package parsso.idman.models.services.serviceType;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.models.services.Service;
import parsso.idman.models.services.servicesSubModel.ExpirationPolicy;
import parsso.idman.models.services.servicesSubModel.Property;
import parsso.idman.models.services.servicesSubModel.ProxyPolicy;
import parsso.idman.models.services.servicesSubModel.UsernameAttributeProvider;

import java.util.LinkedList;

@Setter
@Getter

public class OAuthService extends Service {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private ExpirationPolicy expirationPolicy;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private UsernameAttributeProvider usernameAttributeProvider;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String theme;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private ProxyPolicy proxyPolicy;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Object[] requiredHandlers;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Object[] environments;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String publicKey;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private Property properties;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String clientId;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String clientSecret;
  private Object supportedGrantTypes;
  private Object supportedResponseTypes;

  public OAuthService() {

    super.setAtClass("org.apereo.cas.support.oauth.services.OAuthRegisteredService");
    properties = new Property();

    requiredHandlers = new Object[2];
    requiredHandlers[0] = "java.util.HashSet";
    requiredHandlers[1] = new LinkedList<>();

    environments = new Object[2];
    environments[0] = "java.util.HashSet";
    environments[1] = new LinkedList<>();

    Object[] contacts = new Object[2];
    contacts[0] = "java.util.ArrayList";
    contacts[1] = new LinkedList<>();
    super.setContacts(contacts);

  }
}

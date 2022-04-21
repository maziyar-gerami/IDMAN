package parsso.idman.models.services.servicesSubModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ProxyPolicy {
  @JsonProperty("@class")
  private String atClass;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String pattern;

  public ProxyPolicy() {
    atClass = "org.apereo.cas.services.RefuseRegisteredServiceProxyPolicy";
  }

}

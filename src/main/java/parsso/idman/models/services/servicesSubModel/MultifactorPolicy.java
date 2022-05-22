package parsso.idman.models.services.servicesSubModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class MultifactorPolicy {
  Object[] multifactorAuthenticationProviders;
  String failureMode;
  Boolean bypassEnabled;
  @JsonProperty("@class")
  private String atClass;

  public MultifactorPolicy() {
    multifactorAuthenticationProviders = new Object[2];
    multifactorAuthenticationProviders[0] = "java.util.LinkedHashSet";
    multifactorAuthenticationProviders[1] = new String[1];
    atClass = "org.apereo.cas.services.DefaultRegisteredServiceMultifactorPolicy";
    failureMode = "UNDEFINED";
    bypassEnabled = false;
  }

  public void setMultifactorAuthenticationProviders(String multifactorAuthenticationProviders) {
    String[] temp = new String[1];
    temp[0] = multifactorAuthenticationProviders;
    this.multifactorAuthenticationProviders[1] = temp;
  }
}

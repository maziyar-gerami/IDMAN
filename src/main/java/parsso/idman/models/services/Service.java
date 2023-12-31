package parsso.idman.models.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.javers.core.metamodel.annotation.DiffIgnore;
import parsso.idman.models.services.servicesSubModel.AccessStrategy;
import parsso.idman.models.services.servicesSubModel.AttributeReleasePolicy;
import parsso.idman.models.services.servicesSubModel.ExtraInfo;
import parsso.idman.models.services.servicesSubModel.MultifactorPolicy;

@Setter
@Getter
public class Service implements Comparable<Service> {
  private long id;
  private String name;
  @JsonProperty("@class")
  private String atClass;
  private String serviceId;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private int evaluationOrder;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @DiffIgnore
  private AccessStrategy accessStrategy;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MultifactorPolicy multifactorPolicy;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private AttributeReleasePolicy attributeReleasePolicy;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Object[] contacts;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String description;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String logoutUrl;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String logoutType;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String informationUrl;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String privacyUrl;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String logo;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private ExtraInfo extraInfo;

  @Override
  public int compareTo(Service second) {
    return Long.compare(second.getId(), this.getId());
  }
}

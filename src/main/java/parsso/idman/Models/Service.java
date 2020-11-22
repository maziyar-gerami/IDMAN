package parsso.idman.Models;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.ServicesSubModel.AccessStrategy;
import parsso.idman.Models.ServicesSubModel.AttributeReleasePolicy;
import parsso.idman.Models.ServicesSubModel.MultifactorPolicy;

@Setter
@Getter
public class Service {
    private long id;
    private String name;
    @JsonProperty("@class")
    private String atClass;
    private String serviceId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int evaluationOrder;
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
}

package parsso.idman.Models.Services.ServicesSubModel;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ExpirationPolicy {
    @JsonProperty("@class")
    private String atClass;
    private boolean deleteWhenExpired;
    private boolean notifyWhenDeleted;

    public ExpirationPolicy() {
        atClass = "org.apereo.cas.services.DefaultRegisteredServiceExpirationPolicy";
        deleteWhenExpired = false;
        notifyWhenDeleted = false;

    }
}

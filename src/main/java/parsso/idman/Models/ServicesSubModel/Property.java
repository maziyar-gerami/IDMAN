package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class Property {

    @JsonProperty("@class")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String atClass;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Email email;
    public Property() {
        atClass = "java.util.LinkedHashMap";

    }

}

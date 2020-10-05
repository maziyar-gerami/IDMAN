package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

@Setter
@Getter

public class Property {

    public Property(){
        atClass = "java.util.LinkedHashMap";

    }
    @JsonProperty("@class")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String atClass;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Email email;

}

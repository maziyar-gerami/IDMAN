package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequiredAttributes {
        public RequiredAttributes(){
                atClass = "java.util.HashMap";
        }
        @JsonProperty("@class")
        private String atClass;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Object[] cn;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String givenName;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Object[] member;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Object[] grouperAttributes;
}

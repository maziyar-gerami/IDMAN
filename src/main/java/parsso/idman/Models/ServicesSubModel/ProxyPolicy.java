package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ProxyPolicy {

        public ProxyPolicy(){
                atClass = "org.apereo.cas.services.RefuseRegisteredServiceProxyPolicy";
        }


        @JsonProperty("@class")
        private String atClass;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String pattern;

}
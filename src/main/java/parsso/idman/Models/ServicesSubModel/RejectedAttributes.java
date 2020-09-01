package parsso.idman.Models.ServicesSubModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class RejectedAttributes {

        public RejectedAttributes(){
                atClass = "java.util.HashMap";
        }
        @JsonProperty("@class")
        private String atClass;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<Object> role;

}

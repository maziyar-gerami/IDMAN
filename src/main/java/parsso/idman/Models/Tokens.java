package parsso.idman.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class Tokens {
    @JsonIgnore
    private ObjectId _id;
    @JsonIgnore
    private String userId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mobileToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resetPassToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String qrToken;
}

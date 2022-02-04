package parsso.idman.models.logs;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Transcript {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object users;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object groups;

}

package parsso.idman.Models.Logs;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Transcript {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object licensed;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object unLicensed;


    public Transcript(Object licensed, Object unLicensed) {
        this.licensed = licensed;
        this.unLicensed = unLicensed;
    }

    public Transcript() {

    }
}

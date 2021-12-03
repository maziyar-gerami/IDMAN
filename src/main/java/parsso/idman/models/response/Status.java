package parsso.idman.models.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import parsso.idman.models.other.StringResult;

@Setter
@Getter
public class Status {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String result;
    @JsonIgnore
    String lang;
    @JsonIgnore
    String model;

    public String getResult(){
        return StringResult.get(model,lang);
    }

    public Status(int code) {
        this.code = code;

    }
    public Status (HttpStatus httpStatus, String model, String lang){
        code = httpStatus.value();
        result = StringResult.get(model,lang);

        if (code == 200) {
            result = StringResult.COMMON_200_EN;
        } else if (code ==201){

        } else if (code ==202){

        } else if (code ==203){

        }else  if (code == 204){

        }
    }
}
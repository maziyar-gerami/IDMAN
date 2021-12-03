package parsso.idman.models.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import parsso.idman.models.other.StringResult;

@Setter
@Getter
public class Status {
    int code;
    String result;
    String lang;
    String model;

    public String getResult(){
        return StringResult.get(model,lang);
    }

    public Status(int code) {
        this.code = code;
        if (code == 200) {
            result = StringResult.COMMON_200_EN;
        } else if (code ==201){

        } else if (code ==202){

        } else if (code ==203){

        }else  if (code == 204){

        }
    }
    public Status (HttpStatus httpStatus, String model, String lang){
        code = httpStatus.value();
        result = StringResult.get(model,lang);
    }
}
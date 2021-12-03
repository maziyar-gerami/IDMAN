package parsso.idman.models.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Setter
@Getter
public class Response {
    @JsonIgnore
    String lang;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Status status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object data;
    @JsonIgnore
    String model;

    public Response(HttpStatus httpStatus,Object data,String model, String lang) {
        this.status = new Status(httpStatus,model,lang);
        this.data = data;
    }

    public Response(HttpStatus httpStatus,String model, String lang) {
        this.status = new Status(httpStatus,model,lang);
    }
}

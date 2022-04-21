package parsso.idman.models.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.models.other.StringResult;

@Setter
@Getter
public class Response {
  Status status;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  Object data;

  public Response(Object object, String model, int code, String lang)
      throws NoSuchFieldException, IllegalAccessException {
    this.data = object;
    this.status = new Status(model, code, lang);
  }

  @Setter
  @Getter
  private static class Status {
    int code;
    String result;
    @JsonIgnore
    String model;

    public Status(String model, int code, String lang) throws NoSuchFieldException, IllegalAccessException {
      this.code = code;
      this.model = model;
      this.result = StringResult.get(code, model, lang);
    }
  }
}

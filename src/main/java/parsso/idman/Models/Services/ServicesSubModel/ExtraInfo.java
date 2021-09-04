package parsso.idman.Models.Services.ServicesSubModel;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExtraInfo {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private  String notificationApiURL;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private  String notificationApiKey;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    long id;
    String url;
    int position;
    String UUID;

    public ExtraInfo() {
        position = 0;
    }

}

package parsso.idman.Helpers.Events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import ua_parser.Client;
@Getter
@Setter
@JsonDeserialize
public class AgentInfo {
    String os;
    String browser;

    public AgentInfo(Client client) {

        try {
            this.os = client.os.family + " " + client.os.major;
        } catch (Exception e){
            this.os = "Unknown";
        }
        try {
            this.browser = client.userAgent.family + " " + client.userAgent.major + "." + client.userAgent.minor;
        }catch (Exception e){
            this.browser = "Unknown";
        }
    }
}

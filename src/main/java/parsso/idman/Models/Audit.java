package parsso.idman.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

@Setter
@Getter
public class Audit {
    ObjectId _id;
    private String principal;
    private String resourceOperatedUpon;
    private String actionPerformed;
    private String applicationCode;
    private Date whenActionWasPerformed;
    private String clientIpAddress;
    @JsonIgnore
    private String serverIpAddress;

}

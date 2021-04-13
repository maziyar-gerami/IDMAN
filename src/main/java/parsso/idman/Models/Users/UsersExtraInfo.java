package parsso.idman.Models.Users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import java.util.UUID;

import java.util.Date;

@Getter
@Setter
public class UsersExtraInfo {
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
    private long creationTimeStamp;
    private String photoName;
    private  boolean unDeletable;
    @JsonIgnore
    private String role;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String category;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String subCategory;

    public UsersExtraInfo(){

    }

    public UsersExtraInfo(String userId) {
        this.userId = userId;
        this.qrToken = UUID.randomUUID().toString();
        this.unDeletable = false;
        this.creationTimeStamp = new Date().getTime();
    }

    public UsersExtraInfo(String userId, String photoName, boolean unDeletable) {
        this.userId = userId;
        this.qrToken = UUID.randomUUID().toString();
        this.creationTimeStamp = new Date().getTime();
        this.photoName = photoName;
        this.unDeletable = unDeletable;
        this.role = "USER";
    }

}

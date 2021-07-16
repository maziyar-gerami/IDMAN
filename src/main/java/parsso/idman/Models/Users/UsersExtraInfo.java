package parsso.idman.Models.Users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class UsersExtraInfo {
    @JsonIgnore
    private ObjectId _id;
    private String userId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mobileToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resetPassToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String qrToken;
    private long creationTimeStamp;
    private String photoName;
    private boolean unDeletable;
    private String role;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String category;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String subCategory;

    private String displayName;
    private long timeStamp;
    private List<String> memberOf;
    private String status;
    private long passwordChangedTime;


    public UsersExtraInfo() {

    }

    public UsersExtraInfo(User user) {
        this._id = user.get_id();
        this.userId = user.getUserId().trim();
        this.displayName = user.getDisplayName().trim();
        this.memberOf = user.getMemberOf();
        this.passwordChangedTime = user.getPasswordChangedTime();
        this.status = user.getStatus();
        this.timeStamp = user.getTimeStamp();
    }

    public UsersExtraInfo(String userId) {
        this.userId = userId;
        this.qrToken = UUID.randomUUID().toString();
        this.unDeletable = false;
        if (userId.equalsIgnoreCase("su"))
            this.role = "SUPERADMIN";
        else
            this.role = "USER";
        this.creationTimeStamp = new Date().getTime();
    }

    public UsersExtraInfo(User user, String photoName, boolean unDeletable) {
        this._id = user.get_id();
        this.userId = user.getUserId().trim();
        this.displayName = user.getDisplayName().trim();
        this.memberOf = user.getMemberOf();
        this.passwordChangedTime = user.getPasswordChangedTime();
        this.status = user.getStatus();
        this.timeStamp = user.getTimeStamp();

        this.qrToken = UUID.randomUUID().toString();
        this.creationTimeStamp = new Date().getTime();
        this.photoName = photoName;
        this.unDeletable = unDeletable;
        this.role = "USER";
    }

    public List<String> getMemberOf() {
        if (!(memberOf == null || memberOf.size() == 0))
            return memberOf.stream().distinct().collect(Collectors.toList());
        return null;
    }
}

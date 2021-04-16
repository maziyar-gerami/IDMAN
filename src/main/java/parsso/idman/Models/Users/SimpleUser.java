package parsso.idman.Models.Users;


import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SimpleUser implements Serializable, Comparable {

    public SimpleUser(User user) {
        this._id = user.get_id();
        this.userId = user.getUserId();
        this.displayName = user.getDisplayName();
        this.memberOf = user.getMemberOf();
        this.passwordChangedTime = user.getPasswordChangedTime();
        this.status = user.getStatus();
        this.timeStamp = user.getTimeStamp();
    }

    public SimpleUser() {

    }

    private ObjectId _id;
    private String userId;
    private String displayName;
    private long timeStamp;
    private List<String> memberOf;
    private String status;
    private long passwordChangedTime;

    @Override
    public int compareTo(Object second) {
        if (this.timeStamp > ((SimpleUser) second).timeStamp)
            return -1;
        else if (this.timeStamp < ((SimpleUser) second).timeStamp)
            return 1;
        else
            return 0;
    }


}

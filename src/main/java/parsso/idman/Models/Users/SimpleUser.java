package parsso.idman.Models.Users;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class SimpleUser implements Serializable, Comparable {
    public static Comparator<SimpleUser> uidMinToMaxComparator = new Comparator<SimpleUser>() {

        @Override
        public int compare(SimpleUser o1, SimpleUser o2) {
            return o1.getUserId().compareTo(o2.getUserId());
        }

    };
    public static Comparator<SimpleUser> uidMaxToMinComparator = new Comparator<SimpleUser>() {

        @Override
        public int compare(SimpleUser o1, SimpleUser o2) {
            return o2.getUserId().compareTo(o1.getUserId());
        }

    };
    public static Comparator<SimpleUser> displayNameMinToMaxComparator = new Comparator<SimpleUser>() {

        @Override
        public int compare(SimpleUser o1, SimpleUser o2) {
            return o1.getDisplayName().compareTo(o2.getDisplayName());
        }

    };
    public static Comparator<SimpleUser> displayNameMaxToMinComparator = new Comparator<SimpleUser>() {

        @Override
        public int compare(SimpleUser o1, SimpleUser o2) {
            return o2.getDisplayName().compareTo(o1.getDisplayName());
        }

    };
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

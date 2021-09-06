package parsso.idman.Models.Services;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Models.Time;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter

public class ServiceGist {
    private long count;
    private List<Notification> notifications;

    public ServiceGist(){
        List<Notification> nl = new LinkedList<>();

        Notification n1 = new Notification();
        n1.setTitle("n1_t1");
        n1.setMessage("n1_m1");
        n1.setTimestamp(new Long(new Date().getTime()));
        n1.setTime(TimeHelper.longToPersianTime(n1.getTimestamp()));
        nl.add(n1);

        Notification n2 = new Notification();
        n2.setTitle("n2_t2");
        n2.setMessage("n2_m2");
        n2.setTimestamp(new Long(new Date().getTime()));
        n2.setTime(TimeHelper.longToPersianTime(n2.getTimestamp()));
        nl.add(n2);


        Notification n3 = new Notification();
        n3.setTitle("n3_t3");
        n3.setMessage("n3_m3");
        n3.setTimestamp(new Long(new Date().getTime()));
        n3.setTime(TimeHelper.longToPersianTime(n3.getTimestamp()));
        nl.add(n3);

        Collections.sort(nl);

        this.setNotifications(nl);
    }

    @Setter
    @Getter
    private class Notification implements Comparable{
        private String title;
        private String message;
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private long timestamp;
        private Time time;

        @Override
        public int compareTo(Object o) {
            if (this.timestamp> ((Notification) o).getTimestamp())
                return  1;
            else
                return -1;
        }
    }
}

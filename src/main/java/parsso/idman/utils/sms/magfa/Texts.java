package parsso.idman.utils.sms.magfa;


import lombok.Getter;
import parsso.idman.models.other.Time;
import parsso.idman.models.users.User;

import java.util.Date;

@Getter
public class Texts {
    public String mainMessage;

    public void authorizeMessage(String mainCode) {
        String p1 = "کاربر گرامی";
        String p2 = "کد پیامکی شما ";
        String p3 = " می باشد.";
        String p4 = "فاوای فلات قاره";
        this.mainMessage = p1 +"\n"+p2+ mainCode + p3+"\n"+p4;
    }

    public void passwordChangeNotification(User user) {
        String p1 = "هشدار";
        String p2 = "گذرواژه شما در تاریخ ";
        String p3 = " ساعت ";
        String p4 = " تغییر یافت.";
        String p5 = "فاوای فلات قاره";
        Time time =  new Time().longToPersianTime(new Date().getTime());

        this.mainMessage = p1 +"\n" +
                p2 +String.format("%02d",time.getDay())+"-"+String.format("%02d",time.getMonth())+ "-"+time.getYear() + p3 +
                String.format("%02d",time.getHours())+":"+String.format("%02d",time.getMinutes()) + p4+"\n"+p5;
    }


}

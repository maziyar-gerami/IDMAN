package parsso.idman.utils.sms.magfa;


import lombok.Getter;
import parsso.idman.helpers.TimeHelper;
import parsso.idman.models.other.Time;
import parsso.idman.models.users.User;

import java.util.Date;

@Getter
public class Texts {
    public String mainMessage;

    public void authorizeMessage(String mainCode) {
        String p1 = "سلام. لطفا از کد ";
        String p2 = " جهت احراز هویت و ورود به پارسو استفاده نمایید.";
        this.mainMessage = p1 + mainCode + p2;
    }

    public void passwordChangeNotification(User user) {
        String p1 = "هشدار";
        String p2 = "گذرواژه شما در تاریخ ";
        String p3 = " ساعت ";
        String p4 = " تغییر یافت.";
        String p5 = "فاوای فلات قاره";
        Time time =  TimeHelper.longToPersianTime(new Date().getTime());
        String username;
        try {
            username =user.getDisplayName().substring(0, user.getDisplayName().indexOf(' '));
        }catch (Exception e){
            username = user.getDisplayName();
        }
        this.mainMessage = p1 +"\n" +
                p2 +String.format("%02d",time.getDay())+"-"+String.format("%02d",time.getMonth())+ "-"+time.getYear() + p3 +
                String.format("%02d",time.getHours())+":"+String.format("%02d",time.getMinutes()) + p4+"\n"+p5;
    }


}

package parsso.idman.Helpers;


import parsso.idman.Models.Users.User;

public class Variables {


    public static String col_captchas = "IDMAN_Captchas";
    public static String col_events = "IDMAN_Events";
    public static String col_extraInfo = "IDMAN_ExtraInfo";
    public static String col_log = "IDMAN_Log";
    public static String col_properties = "IDMAN_Properties";
    public static String col_propertiesBackup = "IDMAN_PropertiesBackup";
    public static String col_publicMessage = "IDMAN_PublicMessage";
    public static String col_usersExtraInfo = "IDMAN_UserExtraInfo";
    public static String col_services = "IDMAN_Services";
    public static String col_servicesExtraInfo = "IDMAN_ServicesExtraInfo";
    public static String col_tickets = "IDMAN_Tickets";


    public static String email_recoverySubject = "بازنشانی رمز عبور";
    static String email_body = " عزیز \nشما این پیام را مبنی بر بازنشانی رمز عبور برای نام کاربری زیر دریافت نموده اید.\n" +
            "در صورتی که این درخواست از طرف شما انجام نشده است، از این پیام صرف نظر کنید.\n" +
            "در غیر این صورت با کلیک بر روی  لینک زیر نسبت به بازنشانی و تغییر رمز عبور خود اقدام نمایید.\n";
    static String email_end = "\n\nاگر این لینک برای شما بصورت یک لینک قابل کلیک نشان داده نشد، آن را عینا در مرورگر خود کپی کنید";

    static String email_stringUid = "نام کاربری: ";
    static String email_stringLink = "لینک بازنشانی رمز عبور: ";


    public static String template(User user, String url){

        return user.getDisplayName() + email_body + email_stringUid +user.getDisplayName().substring(0, user.getDisplayName().indexOf(' ')) +"\n" +email_stringLink + url + email_end;
    }

}

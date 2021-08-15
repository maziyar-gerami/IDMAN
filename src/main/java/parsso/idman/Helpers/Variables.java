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
    public static String col_usersExtraInfo = "IDMAN_UsersExtraInfo";
    public static String col_services = "IDMAN_Services";
    public static String col_servicesExtraInfo = "IDMAN_ServicesExtraInfo";
    public static String col_tickets = "IDMAN_Tickets";

    public static String RESULT_FAILED = "Failed";
    public static String RESULT_SUCCESS = "Success";

    public static String ACTION_CREATE = "Create";
    public static String ACTION_UPDATE = "Update";
    public static String ACTION_INSERT = "Insert";
    public static String ACTION_DELETE = "Delete";
    public static String ACTION_Retrieve = "Retrieve";
    public static final String ACTION_GET ="Get" ;


    public static String MODEL_USER = "User";
    public static String MODEL_SERVICE = "Service";
    public static String MODEL_GROUP = "Group";
    public static String MODEL_TICKETING = "Ticketing";
    public static String MODEL_ROLE = "Role";




    public static String email_recoverySubject = "بازنشانی رمز عبور";
    public static String ZONE = "Asia/Tehran";
    public static String col_idmanlog = "IDMAN_IdmanLog";
    static String email_body = "شما این پیام را مبنی بر بازنشانی رمز عبور برای نام کاربری زیر دریافت نموده اید." +
            "در صورتی که این درخواست از طرف شما انجام نشده است، از این پیام صرف نظر کنید.\n" +
            "در غیر این صورت با کلیک بر روی  لینک زیر نسبت به بازنشانی و تغییر رمز عبور خود اقدام نمایید.\n";
    static String email_end = "\n\nاگر این لینک برای شما بصورت یک لینک قابل کلیک نشان داده نشد، آن را عینا در مرورگر خود کپی کنید";
    static String email_stringUid = "نام کاربری: ";
    static String email_stringLink = "لینک بازنشانی رمز عبور: ";
    static String logo = "https://blog.pars-sso.ir/wp-content/uploads/2021/02/magit-logo.png";

    public static String template(User user, String url) {

        return

                "<p style=\"text-align:right;\" dir=\"rtl\"><b>" + user.getDisplayName().substring(0, user.getDisplayName().indexOf(' ')) + " عزیز " + "</b></p>" +
                        "<p style=\"text-align:right;\" dir=\"rtl\">" + email_body + "</p>" +
                        "<p style=\"text-align:right;\" dir=\"rtl\">" + email_stringUid + user.getUserId() + "</p>" +
                        "<p style=\"text-align:right;\" dir=\"rtl\">" + email_stringLink + "<a href=" + url + ">" + url + "</a></p>" +
                        "<p style=\"text-align:right;\" dir=\"rtl\">" + email_end + "</p>" +
                        "<img style=\"vertical-align:middle;\" src=\"" + logo + "\"" + "alt= \"Parsso\">";
    }

}


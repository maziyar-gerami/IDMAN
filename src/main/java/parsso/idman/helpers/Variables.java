package parsso.idman.helpers;


import parsso.idman.models.users.User;

public class Variables {
    public static final String col_casEvent = "MongoDbCasEventRepository";
    public static final String col_OneTime = "IDMAN_OneTime";
    public static final String ACTION_EXPIREPASSWORD = "Expire Password";
    public static final String ACTION_REPLACE = "Replace";
    public static final String DOER_SYSTEM = "SYSTEM";
    public static final String ACTION_PARSE = "Parse";
    public static final String ATTR_STATUS = "Change Status";
    public static final String ACTION_DISBLAE = "Disable";
    public static final String ACTION_UNLOCK = "Unlock";
    public static final String ATTR_DEVICEID = "DeviceID";
    public static final String ATTR_LOGGEDIN = "Logged-In";
    public static final String ATTR_PASSWORD = "Password";
    public static final String ACTION_RESET = "Reset";
    public static final String ACTION_CLOSE = "Close";
    public static final String ACTION_REOPEN = "Reopen";
    public static final String ACTION_REFRESH = "Refresh";
    public static final String ACTION_LOCK = "Lock";
    public static final String ACTION_RESTORE = "Restore";
    public static final String MODEL_CONFIG = "Config";
    public static final String ACTION_GET = "Get";
    public static final String LEVEL_INFO = "INFO";
    public static final String LEVEL_WARN = "WARN";
    public static final String LEVEL_ERROR = "ERROR";
    public static final String MODEL_CAPTCHA = "Captcha";
    public static final String STATUS_ENABLE = "Enable";
    public static final String STATUS_CHANGE = "Change";
    public static final String ACTION_REMOVE = "Remove";
    public static final String col_captchas = "IDMAN_Captchas";
    public static final String col_extraInfo = "IDMAN_ExtraInfo";
    public static final String col_properties = "IDMAN_Properties";
    public static final String col_publicMessage = "IDMAN_PublicMessage";
    public static final String col_usersExtraInfo = "IDMAN_UsersExtraInfo";
    public static final String col_services = "IDMAN_Services";
    public static final String col_servicesExtraInfo = "IDMAN_ServicesExtraInfo";
    public static final String col_audit = "MongoDbCasAuditRepository";
    public static final String col_tickets = "IDMAN_Tickets";
    public static final String RESULT_FAILED = "Failed";
    public static final String RESULT_SUCCESS = "Success";
    public static final String RESULT_FINISHED = "Finished";
    public static final String RESULT_STARTED = "Started";
    public static final String ACTION_CREATE = "Create";
    public static final String ACTION_UPDATE = "Update";
    public static final String ACTION_INSERT = "Insert";
    public static final String ACTION_DELETE = "Delete";
    public static final String ACTION_SET = "Set";
    public static final String ACTION_ADD = "Add";
    public static final String ACTION_RETRIEVE = "Retrieve";
    public static final String MODEL_USER = "user";
    public static final String MODEL_SERVICE = "service";
    public static final String MODEL_GROUP = "group";
    public static final String MODEL_TICKETING = "Ticketing";
    public static final String email_recoverySubject = "بازنشانی رمز عبور";
    public static final String ZONE = "Asia/Tehran";
    public static final String col_idmanLog = "IDMAN_IdmanLog";
    public static final String col_Log = "IDMAN_Log";
    public static final String ACCESS_ADD = "Access Add";
    public static final String ACCESS_REM = "Access Remove";
    public static final String ACCESS_STRATEGY = "Access Strategy";
    public static final String MSG_FA_CODE_200 = "پاسخ ارسال شد.";
    public static final String MSG_FA_CODE_400 = "بدنه JSON ارسال شده از API  فرمتی نادرست دارد.";
    public static final String MSG_FA_CODE_501 = "بدنه JSON ارسال شده از API  مطابق استاندارد نیست";
    public static final String MSG_FA_CODE_405 = "فرمت آدرس API صحیح نیست.";
    public static final String MSG_FA_CODE_503 = "آدرس API صحیح نیست و یا متد آن به درستی تعیین نشده است.";
    public static final String MODEL_SETTINGS = "Settings";
    static final String email_body = "شما این پیام را مبنی بر بازنشانی رمز عبور برای نام کاربری زیر دریافت نموده اید." +
            "در صورتی که این درخواست از طرف شما انجام نشده است، از این پیام صرف نظر کنید.\n" +
            "در غیر این صورت با کلیک بر روی  لینک زیر نسبت به بازنشانی و تغییر رمز عبور خود اقدام نمایید.\n";
    static final String email_end = "\n\nاگر این لینک برای شما بصورت یک لینک قابل کلیک نشان داده نشد، آن را عینا در مرورگر خود کپی کنید";
    static final String email_stringUid = "نام کاربری: ";
    static final String email_stringLink = "لینک بازنشانی رمز عبور: ";
    static final String logo = "https://blog.pars-sso.ir/wp-content/uploads/2021/02/magit-logo.png";
    public static final String col_GoogleAuthDevice= "MongoDbGoogleAuthenticatorRepository";
    public static long LOGS_LIMIT = 10000;

    public static String template(User user, String url) {

        return

                "<p style=\"text-align:right;\" dir=\"rtl\"><b>" + user.getDisplayName().substring(0, user.getDisplayName().indexOf(' ')) + " عزیز " + "</b></p>" +
                        "<p style=\"text-align:right;\" dir=\"rtl\">" + email_body + "</p>" +
                        "<p style=\"text-align:right;\" dir=\"rtl\">" + email_stringUid + user.getUserId() + "</p>" +
                        "<p style=\"text-align:right;\" dir=\"rtl\">" + email_stringLink + "<a href=" + url + ">" + url + "</a></p>" +
                        "<p style=\"text-align:right;\" dir=\"rtl\">" + email_end + "</p>" +
                        "<img style=\"vertical-align:middle;\" src=\"" + logo + "\"" + "alt= \"Parsso\">";
    }

    public static String PARSSO_IDMAN=
                    " ███████████   █████████  ███████████    █████████  █████████     ███████   \n" +
                    "░░███░░░░░███ ███░░░░░███░░███░░░░░███  ███░░░░░██████░░░░░███  ███░░░░░███ \n" +
                    " ░███    ░███░███    ░███ ░███    ░███ ░███    ░░░░███    ░░░  ███     ░░███\n" +
                    " ░██████████ ░███████████ ░██████████  ░░█████████░░█████████ ░███      ░███\n" +
                    " ░███░░░░░░  ░███░░░░░███ ░███░░░░░███  ░░░░░░░░███░░░░░░░░███░███      ░███\n" +
                    " ░███        ░███    ░███ ░███    ░███  ███    ░██████    ░███░░███     ███ \n" +
                    " █████       █████   ██████████   █████░░█████████░░█████████  ░░░███████░  \n" +
                    "░░░░░       ░░░░░   ░░░░░░░░░░   ░░░░░  ░░░░░░░░░  ░░░░░░░░░     ░░░░░░░    \n" +
                    " ███████████████  ██████   ██████ █████████  ██████   █████                 \n" +
                    "░░███░░███░░░░███░░██████ ██████ ███░░░░░███░░██████ ░░███                  \n" +
                    " ░███ ░███   ░░███░███░█████░███░███    ░███ ░███░███ ░███                  \n" +
                    " ░███ ░███    ░███░███░░███ ░███░███████████ ░███░░███░███                  \n" +
                    " ░███ ░███    ░███░███ ░░░  ░███░███░░░░░███ ░███ ░░██████                  \n" +
                    " ░███ ░███    ███ ░███      ░███░███    ░███ ░███  ░░█████                  \n" +
                    " ███████████████  █████     █████████   ██████████  ░░█████                 \n" +
                    "░░░░░░░░░░░░░░░  ░░░░░     ░░░░░░░░░   ░░░░░░░░░░    ░░░░░                  \n" +
                    "                                                                            \n" +
                    "                                                                            \n" +
                    "                                                                            ";

}


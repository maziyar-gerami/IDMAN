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
    public static final String col_devices = "MongoDbGoogleAuthenticatorRepository";
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
    public static final String MODEL_TOKEN = "Token";

    public static final String SMS_MAGFA_USERNAME = "SMS.Magfa.username";
    public static final String SMS_MAGFA_PASSWORD = "SMS.Magfa.password";
    public static final String SKYROOM_API_KEY = "skyroom.api.key";
    public static final String USER_PROFILE_ACCESS = "user.profile.access";
    public static final String DEFAULT_USER_PASSWORD = "default.user.password";
    public static final String CAPTCHA_LENGTH = "captcha.length";
    public static final String SKYROOM_ENABLE = "skyroom.enable";
    public static final String SMS_SDK = "SMS.SDK";
    public static final String TOKEN_VALID_SMS = "token.valid.SMS";
    public static final String KAVENEGAR_API_KEY = "kavenegar.sms.api.key";
    public static final String PASSWORD_CHANGE_NOTIFICATION = "password.change.notification";
    public static final String METADATA_PATH = "metadata.file.path";
    public static final String QR_DEVICES_PATH = "qr.devices.path";
    public static final String SERVICE_FOLDER_PATH = "services.folder.path";
    public static final String PROFILE_PHOTO_PATH = "profile.photo.path";
    public static final String ACTION_BACKUP = "Backup";
    public static final String ACTION_RESET_FACTORY = "Reset Factory";
    public static final String SMS_VALIDATION_DIGITS = "sms.validation.digits";
    public static final String SERVICES_ICON_PATH = "services.icon.path";
    public static final String PASSWORD_CHANGE_LIMIT = "password.change.limit";
    public static final String PASSWORD_CHANGE_LIMIT_NUMBER = "password.change.limit.number";
    public static final String TOKEN_VALID_EMAIL = "token.valid.email";

    static final String email_body = "شما این پیام را مبنی بر بازنشانی رمز عبور برای نام کاربری زیر دریافت نموده اید." +
            "در صورتی که این درخواست از طرف شما انجام نشده است، از این پیام صرف نظر کنید.\n" +
            "در غیر این صورت با کلیک بر روی  لینک زیر نسبت به بازنشانی و تغییر رمز عبور خود اقدام نمایید.\n";
    static final String email_end = "\n\nاگر این لینک برای شما بصورت یک لینک قابل کلیک نشان داده نشد، آن را عینا در مرورگر خود کپی کنید";
    static final String email_stringUid = "نام کاربری: ";
    static final String email_stringLink = "لینک بازنشانی رمز عبور: ";
    static final String logo = "https://blog.pars-sso.ir/wp-content/uploads/2021/02/magit-logo.png";
    public static final String col_GoogleAuthDevice = "MongoDbGoogleAuthenticatorRepository";
    public static final int PER_BATCH_COUNT = 1000;
    public static final String col_Token = "Tokens";
    public static String col_propertiesBackup = "IDMAN_PropertiesBackup";

    public static String template(User user, String url) {

        return

                "<p style=\"text-align:right;\" dir=\"rtl\"><b>" + user.getDisplayName().substring(0, user.getDisplayName().indexOf(' ')) + " عزیز " + "</b></p>" +
                        "<p style=\"text-align:right;\" dir=\"rtl\">" + email_body + "</p>" +
                        "<p style=\"text-align:right;\" dir=\"rtl\">" + email_stringUid + user.getUserId() + "</p>" +
                        "<p style=\"text-align:right;\" dir=\"rtl\">" + email_stringLink + "<a href=" + url + ">" + url + "</a></p>" +
                        "<p style=\"text-align:right;\" dir=\"rtl\">" + email_end + "</p>" +
                        "<img style=\"vertical-align:middle;\" src=\"" + logo + "\"" + "alt= \"Parsso\">";
    }

    public static final String PARSSO_IDMAN =
     "___ ____  __  __    _    _   _ \n"
    +"|_ _|  _ \\|  \\/  |  / \\  | \\ | |\n"
     +"| || | | | |\\/| | / _ \\ |  \\| |\n"
     +"| || |_| | |  | |/ ___ \\| |\\  |\n"
    +"|___|____/|_|  |_/_/   \\_|_| \\_|";
                                    

}


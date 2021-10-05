package parsso.idman.Helpers;


import parsso.idman.Models.Users.User;

public class Variables {
	public static final String col_casEvent = "MongoDbCasEventRepository";
	public static final String ACTION_EXPIREPASSWORD = "Expire Password";
	public static final String ACTION_REPLACE = "Replace";
	public static final String DOER_SYSTEM = "SYSTEM";
	public static final String ACTION_PARSE = "Parse";
	public static final String ATTR_STATUS = "Change Status";
	public static final String ACTION_DISBLAE = "Disable";
	public static final String ACTION_UNLOCK = "Unlock";
	public static final String ATTR_DEVICEID = "DeviceID";
	public static final String ATTR_PASSWORD = "Password";
	public static final String ATTR_IMAGE = "Profile Image";
	public static final String ACTION_RESET = "Reset";
	public static final String ACTION_CLOSE = "Close";
	public static final String ACTION_REOPEN = "Reopen";
	public static final String ACTION_REFRESH = "Refresh";
	public static final String ACTION_LOCK = "Lock";
	public static final String ACTION_RESTORE = "Restore";
	public static final String MODEL_CONFIG = "Config";
	public static final String ATTR_USERID = "UserID";
	public static final String ACTION_GET = "Get";
	public static final String LEVEL_INFO = "INFO";
	public static final String LEVEL_WARN = "WARN";
	public static final String LEVEL_ERROR = "ERROR";
	public static final String MODEL_CAPTCHA = "Captcha";
	public static final String STATUS_ENABLE = "Enable";
	public static final String STATUS_DISABLE = "DISABLE";
	public static final String STATUS_UNLOCK = "UNLOCK";
	public static final String STATUS_LOCK = "Lock";
	public static final String STATUS_CHANGE = "Change";
	public static final String ACTION_REMOVE = "Remove";
	public static String col_captchas = "IDMAN_Captchas";
	public static String col_events = "IDMAN_Events";
	public static String col_extraInfo = "IDMAN_ExtraInfo";
	public static String col_properties = "IDMAN_Properties";
	public static String col_propertiesBackup = "IDMAN_PropertiesBackup";
	public static String col_publicMessage = "IDMAN_PublicMessage";
	public static String col_usersExtraInfo = "IDMAN_UsersExtraInfo";
	public static String col_services = "IDMAN_Services";
	public static String col_servicesExtraInfo = "IDMAN_ServicesExtraInfo";
	public static String col_audit = "MongoDbCasAuditRepository";
	public static String col_tickets = "IDMAN_Tickets";
	public static String RESULT_FAILED = "Failed";
	public static String RESULT_SUCCESS = "Success";
	public static String RESULT_FINISHED = "Finished";
	public static String RESULT_STARTED = "Started";
	public static String ACTION_CREATE = "Create";
	public static String ACTION_UPDATE = "Update";
	public static String ACTION_INSERT = "Insert";
	public static String ACTION_DELETE = "Delete";
	public static String ACTION_RETRIEVE = "Retrieve";
	public static String MODEL_USER = "User";
	public static String MODEL_SERVICE = "Service";
	public static String MODEL_GROUP = "Group";
	public static String MODEL_TICKETING = "Ticketing";
	public static String MODEL_ROLE = "Role";
	public static String email_recoverySubject = "بازنشانی رمز عبور";
	public static String ZONE = "Asia/Tehran";
	public static String col_idmanLog = "IDMAN_IdmanLog";
	public static String ACCESS_ADD = "Access Add";
	public static String ACCESS_REM = "Access Remove";
	public static String ACCESS_STRATEGY = "Access Strategy";
	public static String MSG_FA_CODE_200 = "پاسخ ارسال شد.";
	public static String MSG_FA_CODE_400 = "بدنه JSON ارسال شده به API  فرمتی نادرست دارد.";
	public static String MSG_FA_CODE_401 = "نام کاربری ارسال شده صحیح نیست.";
	public static String MSG_FA_CODE_403 = "کلید API وارد شده صحیح نیست.";
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


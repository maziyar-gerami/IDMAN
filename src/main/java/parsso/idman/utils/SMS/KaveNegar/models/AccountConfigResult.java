package parsso.idman.Utils.SMS.KaveNegar.models;


import com.google.gson.JsonObject;

public class AccountConfigResult {
    private final String apiLogs;
    private final String dailyReport;
    private final String debugMode;
    private final String defaultSender;
    private final String resendFailed;
    private final int minCreditAlarm;

    public AccountConfigResult(JsonObject json) {
        this.apiLogs = json.get("apilogs").getAsString();
        this.dailyReport = json.get("dailyreport").getAsString();
        this.debugMode = json.get("debugmode").getAsString();
        this.defaultSender = json.get("DefaultSender").getAsString();
        this.resendFailed = json.get("resendfailed").getAsString();
        this.minCreditAlarm = json.get("Mincreditalarm").getAsInt();
    }

    public String getApiLogs() {
        return apiLogs;
    }

    public String getDailyReport() {
        return dailyReport;
    }

    public String getDebugMode() {
        return debugMode;
    }

    public String getDefaultSender() {
        return defaultSender;
    }

    public String getResendFailed() {
        return resendFailed;
    }

    public int getMinCreditAlarm() {
        return minCreditAlarm;
    }
}

package parsso.idman.utils.sms.kaveNegar.models;


import com.google.gson.JsonObject;
import parsso.idman.utils.sms.kaveNegar.enums.MessageStatus;

public class StatusResult {
    int messageId;
    MessageStatus status;
    String statusText;

    @SuppressWarnings("unused")
    protected StatusResult() {

    }

    public StatusResult(JsonObject json) {
        this.messageId = (json.get("messageid").getAsInt());
        this.status = MessageStatus.valueOf(json.get("status").getAsInt());
        this.statusText = json.get("statustext").getAsString();
    }

    @SuppressWarnings("unused")
    public int getMessageId() {
        return messageId;
    }

    @SuppressWarnings("unused")
    public MessageStatus getStatus() {
        return status;
    }

    @SuppressWarnings("unused")
    public String getStatusText() {
        return statusText;
    }
}

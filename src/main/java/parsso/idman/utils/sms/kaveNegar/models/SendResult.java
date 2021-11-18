package parsso.idman.utils.sms.kaveNegar.models;


import com.google.gson.JsonObject;

public class SendResult {
    private final Long messageId;
    private final String message;
    private final Integer status;
    private final String statusText;
    private final String sender;
    private final String receptor;
    private final Long date;
    private final Integer cost;

    public SendResult(JsonObject json) {
        this.cost = json.get("cost").getAsInt();
        this.date = json.get("date").getAsLong();
        this.messageId = json.get("messageid").getAsLong();
        this.message = json.get("message").getAsString();
        this.receptor = json.get("receptor").getAsString();
        this.status = json.get("status").getAsInt();
        this.statusText = json.get("statustext").getAsString();
        this.sender = json.get("sender").getAsString();
    }

    @SuppressWarnings("unused")
    public long getMessageId() {
        return messageId;
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }

    @SuppressWarnings("unused")
    public int getStatus() {
        return status;
    }

    @SuppressWarnings("unused")
    public String getStatusText() {
        return statusText;
    }

    @SuppressWarnings("unused")
    public String getSender() {
        return sender;
    }

    @SuppressWarnings("unused")
    public String getReceptor() {
        return receptor;
    }

    @SuppressWarnings("unused")
    public long getDate() {
        return date;
    }

    @SuppressWarnings("unused")
    public int getCost() {
        return cost;
    }
}
package parsso.idman.utils.SMS.KaveNegar.models;


import com.google.gson.JsonObject;

public class ReceiveResult {
    private final Long messageId;
    private final String message;
    private final String sender;
    private final String receptor;
    private final Long date;

    public ReceiveResult(JsonObject json) {
        this.messageId = json.get("messageid").getAsLong();
        this.date = json.get("date").getAsLong();
        this.message = json.get("message").getAsString();
        this.sender = json.get("sender").getAsString();
        this.receptor = (json.get("receptor").getAsString());
    }

    public Long getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceptor() {
        return receptor;
    }

    public Long getDate() {
        return date;
    }
}

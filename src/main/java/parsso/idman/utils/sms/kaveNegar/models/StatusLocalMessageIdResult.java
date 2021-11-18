package parsso.idman.utils.sms.kaveNegar.models;


import com.google.gson.JsonObject;

public class StatusLocalMessageIdResult extends StatusResult {
    final long localId;

    public StatusLocalMessageIdResult(JsonObject json) {
        super(json);
        this.localId = json.get("localid").getAsLong();
    }

    @SuppressWarnings("unused")
    public long getLocalId() {
        return localId;
    }
}

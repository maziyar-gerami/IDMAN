package parsso.idman.Utils.SMS.KaveNegar.models;


import com.google.gson.JsonObject;

public class StatusLocalMessageIdResult extends StatusResult {
    final long localId;

    public StatusLocalMessageIdResult(JsonObject json) {
        super(json);
        this.localId = json.get("localid").getAsLong();
    }

    public long getLocalId() {
        return localId;
    }
}

package parsso.idman.Utils.SMS.KaveNegar.models;


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

	public long getMessageId() {
		return messageId;
	}

	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}

	public String getStatusText() {
		return statusText;
	}

	public String getSender() {
		return sender;
	}

	public String getReceptor() {
		return receptor;
	}

	public long getDate() {
		return date;
	}

	public int getCost() {
		return cost;
	}
}
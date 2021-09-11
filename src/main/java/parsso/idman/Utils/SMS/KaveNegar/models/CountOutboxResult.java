package parsso.idman.Utils.SMS.KaveNegar.models;


import com.google.gson.JsonObject;

public class CountOutboxResult extends CountInboxResult {
	private final Long sumPart;
	private final Long cost;

	public CountOutboxResult(JsonObject json) {
		super(json);
		this.sumPart = json.get("sumpart").getAsLong();
		this.cost = json.get("cost").getAsLong();
	}

	public Long getSumPart() {
		return sumPart;
	}

	public Long getCost() {
		return cost;
	}
}
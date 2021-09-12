package parsso.idman.Utils.SMS.KaveNegar.models;


import com.google.gson.JsonObject;

public class CountInboxResult {
	private final Long startDate;
	private final Long endDate;
	private final Long sumCount;

	public CountInboxResult(JsonObject json) {
		this.startDate = json.get("startdate").getAsLong();
		this.endDate = json.get("enddate").getAsLong();
		this.sumCount = json.get("sumcount").getAsLong();
	}

	public Long getStartDate() {
		return startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public Long getSumCount() {
		return sumCount;
	}
}
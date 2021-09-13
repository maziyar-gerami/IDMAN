package parsso.idman.Models.Services;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.Return;
import parsso.idman.Models.Time;

import java.util.List;

@Setter
@Getter

public class ServiceGist {
	@JsonIgnore
	@JsonProperty("return")
	private Return aReturn;
	private int count;
	private List<Notification> notifications;

	@SuppressWarnings("rawtypes")
	@Setter
	@Getter
	private class Notification implements Comparable {
		private String title;
		private String url;
		@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
		private long timestamp;
		private Time time;

		@Override
		public int compareTo(Object o) {
			if (this.timestamp > ((Notification) o).getTimestamp())
				return 1;
			else
				return -1;
		}
	}
}

package parsso.idman.Models.Logs;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.other.Time;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class Report {
	@JsonIgnore
	protected char separator = ':';
	@JsonIgnore
	String level;
	Time dateTime;
	String loggerName;
	String message;
	@JsonIgnore
	String details;
	@JsonIgnore
	Source source;
	@JsonIgnore
	String marker;
	@JsonIgnore
	String threadId;
	@JsonIgnore
	String threadName;
	@JsonIgnore
	String priority;
	@JsonIgnore
	long millis;
	@JsonIgnore
	Date date;
	@JsonIgnore
	String thrown;

	public Time getDateTime() {
		return TimeHelper.longToPersianTime(date.getTime());
	}



	public String getDetails() {
		return source.toString();
	}

	public static List<Report> analyze(MongoTemplate mongoTemplate, int skip, int limit) {
		Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC, "millis"));
		List<Report> le = mongoTemplate.find(query, Report.class,  Variables.col_Log);
		return TimeHelper.reportSetDate(le);
	}

	@Setter
	@Getter
	private class Source {
		String className;
		String methodName;
		String fileName;
		String lineNumber;

		@Override
		public String toString() {
			return
					"className='" + className + separator +
							", methodName='" + methodName + separator +
							", fileName='" + fileName + separator +
							", lineNumber='" + lineNumber + separator;
		}
	}



	@Setter
	@Getter

	public static class ListReports {
		long size;
		int pages;
		List<Report> reportsList;

		public ListReports(long size, int pages, List<Report> relativeEvents) {
			this.size = size;
			this.pages = pages;
			this.reportsList = relativeEvents;
		}

		public ListReports() {

		}

		public ListReports(List<Report> reports, long size, int pages) {
			this.size = size;
			this.pages = pages;
			this.reportsList = reports;
		}
	}


}

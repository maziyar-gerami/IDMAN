package parsso.idman.Models.Logs;


import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
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
public class Audit {
	ObjectId _id;
	private String principal;
	private String resourceOperatedUpon;
	private String actionPerformed;
	private String applicationCode;
	private Date whenActionWasPerformed;
	private String clientIpAddress;
	private String serverIpAddress;
	private Time time;

	public Time getTime() {
		return TimeHelper.longToPersianTime(whenActionWasPerformed.getTime());
	}

	public static List<Audit> analyze(MongoTemplate mongoTemplate, int skip, int limit) {
		Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC, "_id"));
		return mongoTemplate.find(query, Audit.class, Variables.col_audit);
	}

}

package parsso.idman.models.logs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Time;

@Setter
@Getter
public class Audit {
  @JsonProperty("_id")
  ObjectId id;
  private String principal;
  @TextIndexed
  private String resourceOperatedUpon;
  private String applicationCode;
  private String dateString;
  private String timeString;
  private Date whenActionWasPerformed;
  private String clientIpAddress;
  private String serverIpAddress;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String service;

  public static List<Audit> analyze(MongoTemplate mongoTemplate, int skip, int limit) {
    Query query = new Query(Criteria.where("actionPerformed")
        .is("SERVICE_ACCESS_ENFORCEMENT_TRIGGERED").and("principal").ne("audit:unknown").and("resourceOperatedUpon")
        .exists(true)).with(Sort.by(Sort.Direction.DESC, "_id"));
    return mongoTemplate.find(query, Audit.class, Variables.col_audit);
  }

  public static Audit setStringDateAndTime(Audit audit, String date, String time) {
    audit.setDateString(date);
    audit.setTimeString(time);
    return audit;
  }

  public Time getTime() {
    return Time.longToPersianTime(whenActionWasPerformed.getTime());
  }

  @Setter
  @Getter
  public static class ListAudits {
    long size;
    int pages;
    List<Audit> auditList;

    public ListAudits(List<Audit> relativeAudits, long size, int pages) {
      this.size = size;
      this.pages = pages;
      this.auditList = relativeAudits;
    }

    public ListAudits() {

    }
  }

}

package parsso.idman.RepoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Models.ListLogs;
import parsso.idman.Models.Log;
import parsso.idman.Models.Time;
import parsso.idman.Repos.LogRepo;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class LogRepoImpl implements LogRepo {

    private static final String mainCollection = "IDMAN_Log";
    ZoneId zoneId = ZoneId.of("UTC+03:30");

    Instant instant = Instant.now(); //can be LocalDateTime



    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ListLogs getListSizeLogs(int p, int n) {

        List<Log> allLogs = analyze(mainCollection, (p - 1) * n, n);
        long size = mongoTemplate.getCollection(mainCollection).countDocuments();
        for (Log log : allLogs) {
            OffsetDateTime logDate = log.getDate().toInstant()
                    .atOffset(zoneId.getRules().getOffset(instant));
            Time time1 = new Time(logDate.getYear(), logDate.getMonthValue(), logDate.getDayOfMonth(),
                    logDate.getHour(), logDate.getMinute(), logDate.getSecond());
            log.setDateTime(time1);
        }
        return new ListLogs(size, (int) Math.ceil(size / n), allLogs);
    }

    @Override
    public ListLogs getListUserLogs(String userId, int p, int n) {
        Query query = new Query(Criteria.where("loggerName").is(userId))
                .with(Sort.by(Sort.Direction.DESC, "millis"));
        long size = mongoTemplate.count(query, Log.class, mainCollection);

        query.skip((p - 1) * (n)).limit(n);
        List<Log> logList = mongoTemplate.find(query, Log.class, mainCollection);
        for (Log log : logList) {
            OffsetDateTime logDate = log.getDate().toInstant()
                    .atOffset(zoneId.getRules().getOffset(instant));
            Time time1 = new Time(logDate.getYear(), logDate.getMonthValue(), logDate.getDayOfMonth(),
                    logDate.getHour(), logDate.getMinute(), logDate.getSecond());
            log.setDateTime(time1);
        }
        ListLogs listLogs = new ListLogs(size, (int) Math.ceil(size / n), logList);
        return listLogs;
    }

    @Override
    public ListLogs getLogsByDate(String date, int p, int n) {


        String time = new Time(Integer.valueOf(date.substring(4)), Integer.valueOf(date.substring(2, 4)), Integer.valueOf(date.substring(0, 2))).toStringDate();
        String timeStart = time + "T00:00:00.000000" + zoneId.toString().substring(3);
        String timeEnd = time + "T23:59:59.000000" + zoneId.toString().substring(3);

        long logStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(zoneId).toEpochSecond() * 1000;
        long logEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(zoneId).toEpochSecond() * 1000;

        Query query = new Query(Criteria.where("millis").gte(logStartDate).lte(logEndDate));
        List<Log> allLogs = mongoTemplate.find(query, Log.class, mainCollection);

        for (Log log : allLogs) {
            OffsetDateTime logDate = log.getDate().toInstant()
                    .atOffset(zoneId.getRules().getOffset(instant));
            Time time1 = new Time(logDate.getYear(), logDate.getMonthValue(), logDate.getDayOfMonth(),
                    logDate.getHour(), logDate.getMinute(), logDate.getSecond());
            log.setDateTime(time1);
        }


        long size = mongoTemplate.count(query, Log.class, mainCollection);

        return new ListLogs(size, (int) Math.ceil(size / n), allLogs);
    }

    @Override
    public ListLogs getListUserLogByDate(String date, String userId, int skip, int limit) {

        String time = new Time(Integer.valueOf(date.substring(4)), Integer.valueOf(date.substring(2, 4)), Integer.valueOf(date.substring(0, 2))).toStringDate();
        String timeStart = time + "T00:00:00.000000" + zoneId.toString().substring(3);
        String timeEnd = time + "T23:59:59.000000" + zoneId.toString().substring(3);

        long logStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(zoneId).toEpochSecond() * 1000;
        long logEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(zoneId).toEpochSecond() * 1000;
        Query query = new Query(Criteria.where("loggerName").is(userId).and("millis").gte(logStartDate).lte(logEndDate));
        long size = mongoTemplate.count(query, ListLogs.class, mainCollection);
        query.skip((skip - 1) * (limit)).limit(limit).with(Sort.by(Sort.Direction.DESC, "millis"));
        List<Log> logList = mongoTemplate.find(query, Log.class, mainCollection);

        for (Log log : logList) {
            OffsetDateTime logDate = log.getDate().toInstant()
                    .atOffset(zoneId.getRules().getOffset(instant));
            Time time1 = new Time(logDate.getYear(), logDate.getMonthValue(), logDate.getDayOfMonth(),
                    logDate.getHour(), logDate.getMinute(), logDate.getSecond());
            log.setDateTime(time1);
        }

        int pages = (int) Math.ceil(size / limit) + 1;
        ListLogs listLogs = new ListLogs(size, pages, logList);
        return listLogs;
    }

    @Override
    public List<Log> analyze(String collection, int skip, int limit) {
        Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC, "millis"));
        List<Log> le = mongoTemplate.find(query, Log.class, collection);
        for (Log log : le) {
            OffsetDateTime logDate = log.getDate().toInstant()
                    .atOffset(zoneId.getRules().getOffset(instant));
            Time time1 = new Time(logDate.getYear(), logDate.getMonthValue(), logDate.getDayOfMonth(),
                    logDate.getHour(), logDate.getMinute(), logDate.getSecond());
            log.setDateTime(time1);
        }

        return le;
    }
}



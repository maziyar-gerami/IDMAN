package parsso.idman.repoImpls.services.create.sublcasses;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Time;
import parsso.idman.models.services.Period;
import parsso.idman.models.services.Schedule;
import parsso.idman.models.services.SimpleTime;
import parsso.idman.models.services.servicesSubModel.ExtraInfo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ServiceAccess {
    MongoTemplate mongoTemplate;

    public ServiceAccess(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public boolean serviceAccess(long id) {

        Calendar c1 = Calendar.getInstance();
        int currentDay = c1.get(Calendar.DAY_OF_WEEK);

        ExtraInfo extraInfo = mongoTemplate.findOne
                (new Query(Criteria.where("_id").is(id)), ExtraInfo.class, Variables.col_servicesExtraInfo);

        Time time = new Time().longToPersianTime(new Date().getTime());
        SimpleTime simpleTime = new SimpleTime(time);
        List<Schedule> dailyAccess;
        try {
            dailyAccess = Objects.requireNonNull(extraInfo).getDailyAccess();
        } catch (NullPointerException e) {
            return false;
        }
        for (Schedule schedule : dailyAccess)
            if (schedule.getWeekDay() == currentDay) {
                Period period = schedule.getPeriod();
                if (simpleTime.compareTo(period.getFrom()) == 1 && simpleTime.compareTo(period.getTo()) == -1)
                    return true;
            }
        return false;
    }

}

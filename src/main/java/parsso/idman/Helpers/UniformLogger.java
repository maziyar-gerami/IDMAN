package parsso.idman.Helpers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Logs.ReportMessage;

@Service
public class UniformLogger {
    @Autowired
    MongoTemplate mongoTemplate;

    public void record(String doerId, String level, ReportMessage reportMessage) {
        Logger logger = LogManager.getLogger(doerId);
        reportMessage.setLevel(level);

        if (level.equalsIgnoreCase(Variables.LEVEL_WARN))
            logger.warn(reportMessage.toString());
        else if (level.equalsIgnoreCase(Variables.LEVEL_INFO))
            logger.info(reportMessage.toString());
        else if (level.equalsIgnoreCase(Variables.LEVEL_ERROR))
            logger.error(reportMessage.toString());

        mongoTemplate.save(reportMessage, Variables.col_idmanlog);
    }

}

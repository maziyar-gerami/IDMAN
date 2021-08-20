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

    public void warn(String doerId, ReportMessage reportMessage){
        Logger logger = LogManager.getLogger(doerId);
        reportMessage.setLevel(Variables.LEVEL_WARN);
        idmanLogger(doerId, reportMessage);
        logger.warn(reportMessage.toString());
    }

    public void info(String doerId, ReportMessage reportMessage){
        Logger logger = LogManager.getLogger(doerId);
        reportMessage.setLevel(Variables.LEVEL_INFO);
        idmanLogger(doerId, reportMessage);
        logger.info(reportMessage.toString());
    }

    public void error(String doerId, ReportMessage reportMessage){
        Logger logger = LogManager.getLogger(doerId);
        reportMessage.setLevel(Variables.LEVEL_ERROR);
        idmanLogger(doerId, reportMessage);
        logger.error(reportMessage.toString());
    }

    private void idmanLogger(String doerId, ReportMessage reportMessage) {
        Runnable runnable =
                () -> { mongoTemplate.save(reportMessage, Variables.col_idmanlog); };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}

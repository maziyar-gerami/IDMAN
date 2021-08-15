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

    public void record(String doerId,ReportMessage reportMessage){
        Logger logger = LogManager.getLogger(doerId);

        logger.warn(reportMessage.toString());

        mongoTemplate.save(reportMessage, Variables.col_idmanlog);
    }
}

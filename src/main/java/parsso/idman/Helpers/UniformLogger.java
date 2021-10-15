package parsso.idman.Helpers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Logs.Change;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.repos.ServiceRepo;

import java.util.LinkedList;
import java.util.List;

@Service
public class UniformLogger {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ServiceRepo serviceRepo;

    public void warn(String doerId, ReportMessage reportMessage) {
        Logger logger = LogManager.getLogger(doerId);
        reportMessage.setLevel(Variables.LEVEL_WARN);
        reportMessage.setDoerID(doerId);
        idmanLogger(reportMessage);
        logger.warn(reportMessage.toString());
    }

    public void info(String doerId, ReportMessage reportMessage) {
        Logger logger = LogManager.getLogger(doerId);
        reportMessage.setLevel(Variables.LEVEL_INFO);
        reportMessage.setDoerID(doerId);
        idmanLogger(reportMessage);
        logger.warn(reportMessage.toString());
    }

    public void error(String doerId, ReportMessage reportMessage) {
        Logger logger = LogManager.getLogger(doerId);
        reportMessage.setLevel(Variables.LEVEL_ERROR);
        reportMessage.setDoerID(doerId);
        idmanLogger(reportMessage);
        logger.error(reportMessage.toString());
    }

    public void warn(String doerId, ReportMessage reportMessage, Object from, Object to) {
        Logger logger = LogManager.getLogger(doerId);
        reportMessage.setLevel(Variables.LEVEL_WARN);
        reportMessage.setDoerID(doerId);
        idmanLogger(reportMessage);
        logger.warn(reportMessage.toString());
    }

    public void info(String doerId, ReportMessage reportMessage, Object from, Object to) {
        Logger logger = LogManager.getLogger(doerId);
        reportMessage.setLevel(Variables.LEVEL_INFO);
        reportMessage.setDoerID(doerId);
        idmanLogger(reportMessage);
        logger.info(reportMessage.toString());
    }

    public void error(String doerId, ReportMessage reportMessage, Object from, Object to) {
        Logger logger = LogManager.getLogger(doerId);
        reportMessage.setLevel(Variables.LEVEL_ERROR);
        reportMessage.setDoerID(doerId);
        idmanLogger(reportMessage);
        logger.error(reportMessage.toString());
    }

    private void idmanLogger(ReportMessage reportMessage) {
        List<ReportMessage> reportMessageList = new LinkedList<>();

        Runnable runnable =
                () -> {
                    if (reportMessage.getDifference() != null)
                        for (Change ch : reportMessage.getDifference())
                            if (!ch.getAttribute().equalsIgnoreCase("timestamp"))
                                reportMessageList.add(new ReportMessage(ch, reportMessage));


                    if (reportMessage.getUsersGroups() != null) {
                        reportMessage.setInstanceName(serviceRepo.retrieveService(Long.parseLong(reportMessage.getInstance().toString())).getName());
                        for (String s : (List<String>) reportMessage.getUsersGroups().getUsers().getAdd())
                            reportMessageList.add(new ReportMessage(Variables.MODEL_USER, s, Variables.ACCESS_ADD, reportMessage));

                        for (String s : (List<String>) reportMessage.getUsersGroups().getUsers().getRemove())
                            reportMessageList.add(new ReportMessage(Variables.MODEL_USER, s, Variables.ACCESS_REM, reportMessage));

                        for (String s : (List<String>) reportMessage.getUsersGroups().getGroups().getAdd())
                            reportMessageList.add(new ReportMessage(Variables.MODEL_GROUP, s, Variables.ACCESS_ADD, reportMessage));

                        for (String s : (List<String>) reportMessage.getUsersGroups().getGroups().getRemove())
                            reportMessageList.add(new ReportMessage(Variables.MODEL_GROUP, s, Variables.ACCESS_REM, reportMessage));

                    }

                    if (reportMessageList.size() == 0)
                        reportMessageList.add(reportMessage);

                    mongoTemplate.insert(reportMessageList, Variables.col_idmanLog);
                };
        Thread thread = new Thread(runnable);
        thread.start();
    }

}

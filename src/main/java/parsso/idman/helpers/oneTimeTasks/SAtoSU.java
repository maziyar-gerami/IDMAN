package parsso.idman.helpers.oneTimeTasks;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.OneTime;
import parsso.idman.models.users.UsersExtraInfo;

import java.util.Date;
import java.util.List;

public class SAtoSU {

    MongoTemplate mongoTemplate;
    UniformLogger uniformLogger;

    SAtoSU(MongoTemplate mongoTemplate,UniformLogger uniformLogger){
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
    }

    public void run(){
        OneTime oneTime;
        Query query = new Query(Criteria.where("_id").is("SAtoSU"));
        try {
            oneTime = mongoTemplate.findOne(query, OneTime.class,Variables.col_OneTime);
        }catch (NullPointerException e){
            oneTime = new OneTime("SAtoSU",false, 0L);
        }

        if (oneTime== null)
            oneTime= new OneTime("SAtoSU");
        if(oneTime.isRun())
            return;
        List<UsersExtraInfo> usersExtraInfos =  mongoTemplate.find(new Query(Criteria.where("role").is("SUPERADMIN")),UsersExtraInfo.class, Variables.col_usersExtraInfo);
        for (UsersExtraInfo usersExtraInfo :usersExtraInfos){
            usersExtraInfo.setRole("SUPERUSER");
            mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);
        }

        if (!mongoTemplate.getCollectionNames().contains(Variables.col_OneTime))
            mongoTemplate.createCollection(Variables.col_OneTime);

        uniformLogger.info("System",new ReportMessage("Convert", Variables.RESULT_SUCCESS,"SuperAdmin to SuperUser"));

        OneTime oneTime1 = new OneTime("SAtoSU",true,new Date().getTime());
        mongoTemplate.save(oneTime1,Variables.col_OneTime);

    }
}

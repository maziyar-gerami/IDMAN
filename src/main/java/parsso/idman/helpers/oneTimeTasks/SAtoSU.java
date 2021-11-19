package parsso.idman.helpers.oneTimeTasks;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.UsersExtraInfo;

import java.util.List;

public class SAtoSU {

    MongoTemplate mongoTemplate;

    SAtoSU(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public void run(){
        List<UsersExtraInfo> usersExtraInfos =  mongoTemplate.find(new Query(Criteria.where("role").is("SUPERADMIN")),UsersExtraInfo.class, Variables.col_usersExtraInfo);
        for (UsersExtraInfo usersExtraInfo :usersExtraInfos){
            usersExtraInfo.setRole("SUPERUSER");
            mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);
        }

    }
}

package parsso.idman.helpers.oneTimeTasks;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.UserRepo;

import java.util.List;

public class MongoMobile {
    final MongoTemplate mongoTemplate;
    final UserRepo userRepo;

    MongoMobile(MongoTemplate mongoTemplate, UserRepo userRepo) {
        this.mongoTemplate = mongoTemplate;
        this.userRepo = userRepo;
    }

    public void run(){
        long count = mongoTemplate.count(new Query(),Variables.col_usersExtraInfo);
        int number = 50;
        int it = (int) (Math.floorDiv(count,number)+1);
        char[] animationChars = new char[]{'|', '/', '-', '\\'};
        for(int i=0;i<it;i++){
            int skip = i*number;
            List<UsersExtraInfo> usersExtraInfos = userRepo.retrieveUsersMain(skip,number,"","","","","","").getUserList();
            for (UsersExtraInfo usersExtraInfo:usersExtraInfos) {
                String mobile = userRepo.retrieveUsers(usersExtraInfo.getUserId()).getMobile();
                Update update = new Update();
                update.set("mobile",mobile);
                mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(usersExtraInfo.getUserId())), update, Variables.col_usersExtraInfo);
            }
            System.out.print("Adding mobile number to Mongo: " + i + "% " + animationChars[i % 4] + "\r");
        }
        System.out.println("Adding mobile number to Mongo: Done!");

    }
}

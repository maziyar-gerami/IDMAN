package parsso.idman.helpers.oneTimeTasks;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.UsersExtraInfo;

import java.util.List;

public class UserID {
    final MongoTemplate mongoTemplate;
    public UserID(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void run() {
        int count = (int) mongoTemplate.count(new Query(), Variables.col_usersExtraInfo);
        int limit = 1000;
        int pages = (int) Math.ceil(count / (float)limit);

        int c;

        for(int i=0; i<limit; i++){
            if(i==pages)
                limit = count%limit;
            c=i*limit;
            List<UsersExtraInfo> usersExtraInfoList = mongoTemplate.find(new Query().skip(i*limit).limit(limit),UsersExtraInfo.class,Variables.col_usersExtraInfo);
            char[] animationChars = new char[]{'|', '/', '-', '\\'};
            for (UsersExtraInfo usersExtraInfo:usersExtraInfoList) {
                UsersExtraInfo u2 = usersExtraInfo;
                u2.setUserId(null);

                //u2.set_id(usersExtraInfo.getUserId());

                //System.out.print("Processing userId: " + p + "% " + animationChars[p % 4] + "\r");
            }

            //System.out.println("Processing userIDs: Done!");


        }
    }


}

package parsso.idman.helpers.oneTimeTasks;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.UsersExtraInfo;

import java.util.List;

public class RemoveDuplicateExtraInfo {
    final MongoTemplate mongoTemplate;

    public RemoveDuplicateExtraInfo(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void run() {
        int c = 0;
        char[] animationChars = new char[]{'|', '/', '-', '\\'};
        List<UsersExtraInfo> usersExtraInfoList = mongoTemplate.find(new Query(), UsersExtraInfo.class, Variables.col_usersExtraInfo);
        for (UsersExtraInfo usersExtraInfo : usersExtraInfoList) {
            Query query = new Query(Criteria.where("userId").is(usersExtraInfo.getUserId()));
            if (mongoTemplate.count(query, UsersExtraInfo.class, Variables.col_usersExtraInfo) > 1)
                mongoTemplate.save(usersExtraInfo,Variables.col_usersExtraInfo);

            int i = (++c * 100 / usersExtraInfoList.size());
            System.out.print("Processing duplicate users: " + i + "% " + animationChars[i % 4] + "\r");
        }
        System.out.println("Processing duplicate users: Done!");
    }

}

package parsso.idman.helpers.onetime;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.OneTime;
import parsso.idman.models.users.UsersExtraInfo;

import java.util.Date;
import java.util.List;

public class RemoveDuplicateExtraInfo {
  final MongoTemplate mongoTemplate;

  public RemoveDuplicateExtraInfo(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public void run() {
    int c = 0;
    char[] animationChars = new char[] { '|', '/', '-', '\\' };
    List<UsersExtraInfo> usersExtraInfoList = mongoTemplate.find(new Query(), UsersExtraInfo.class,
        Variables.col_usersExtraInfo);
    for (UsersExtraInfo usersExtraInfo : usersExtraInfoList) {
      Query query = new Query(Criteria.where("_id").is(usersExtraInfo.get_id()));
      if (mongoTemplate.count(query, UsersExtraInfo.class, Variables.col_usersExtraInfo) > 1)
        mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);

      int i = (++c * 100 / usersExtraInfoList.size());
      System.out.print("Processing duplicate users: " + i + "% " + animationChars[i % 4] + "\r");
    }

    mongoTemplate.save(new OneTime(Variables.DUPLICATE_USER, true, new Date().getTime()), Variables.col_OneTime);

    System.out.println("Processing duplicate users: Done!");
  }

}

package parsso.idman.helpers.onetime;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.OneTime;
import parsso.idman.models.users.UsersExtraInfo;

import java.util.Date;
import java.util.List;

public class UserID {
  final MongoTemplate mongoTemplate;

  public UserID(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public void run() {
    int count = (int) mongoTemplate.count(new Query(), Variables.col_usersExtraInfo);
    int limit = 1000;
    int pages = (int) Math.ceil(count / (float) limit);

    int c, p;

    for (int i = 0; i < pages; i++) {
      if (i == pages)
        limit = count % limit;
      c = i * limit;

      List<UsersExtraInfo> usersExtraInfoList = mongoTemplate.find(new Query().skip(i * limit).limit(limit),
          UsersExtraInfo.class, Variables.col_usersExtraInfo);
      char[] animationChars = new char[] { '|', '/', '-', '\\' };
      for (UsersExtraInfo usersExtraInfo : usersExtraInfoList) {

        System.out.println(usersExtraInfo.get_id());
        String userId = usersExtraInfo.getUserId().toString();

        usersExtraInfo.setUserId(null);

        usersExtraInfo.set_id(userId);
        mongoTemplate.remove(new Query(Criteria.where("userId").is(userId)), Variables.col_usersExtraInfo);
        mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);

      }

      p = (++c * 100 / count);
      System.out.print("Processing userId: " + p + "% " + animationChars[p % 4] + "\r");

    }

    if (!mongoTemplate.getCollectionNames().contains(Variables.col_OneTime))
    mongoTemplate.createCollection(Variables.col_OneTime);

    mongoTemplate.save(new OneTime(Variables.USERID_TO_ID, true, new Date().getTime()), Variables.col_OneTime);

    System.out.println("Processing userIDs: Done!");

  }

}

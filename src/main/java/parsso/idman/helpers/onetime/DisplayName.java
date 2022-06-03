package parsso.idman.helpers.onetime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import parsso.idman.helpers.Variables;
import parsso.idman.models.other.OneTime;
import parsso.idman.models.users.User;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;
import parsso.idman.repos.users.oprations.sub.UsersUpdateRepo;

import java.util.Date;
import java.util.List;

public class DisplayName {
  final MongoTemplate mongoTemplate;
  final UsersRetrieveRepo usersOpRetrieve;
  final UsersUpdateRepo usersOpUpdate;

  @Autowired
  DisplayName(MongoTemplate mongoTemplate, UsersRetrieveRepo usersOpRetrieve,
      UsersUpdateRepo usersOpUpdate) {
    this.mongoTemplate = mongoTemplate;
    this.usersOpRetrieve = usersOpRetrieve;
    this.usersOpUpdate = usersOpUpdate;
  }

  public void run() {
    int c = 0;
    char[] animationChars = new char[] { '|', '/', '-', '\\' };

    List<User> users = usersOpRetrieve.fullAttributes();
    for (User user : users) {
      if ((user.getDisplayName().contains("آقای") && !user.getDisplayName().contains("آقای "))
          || (user.getDisplayName().contains("خانم") && !user.getDisplayName().contains("خانم "))
          || (user.getDisplayName().contains("اقای") && !user.getDisplayName().contains("اقای "))) {
        String r = user.getDisplayName().substring(0, 4) + " " + user.getDisplayName().substring(4);
        user.setDisplayName(r);
        try {
          user.getUsersExtraInfo().setDisplayName(r);
        } catch (NullPointerException ignored) {
        }

        usersOpUpdate.update("System", user.get_id().toString(), user);
      }
      int i = (++c * 100 / users.size());

      System.out.print("Processing displayName: " + i + "% " + animationChars[i % 4] + "\r");
    }

    OneTime oneTime1 = new OneTime(Variables.DISPLAY_NAME_CORRECTION, true, new Date().getTime());
    mongoTemplate.save(oneTime1, Variables.col_OneTime);

    System.out.println("Processing displayName: Done!");

  }
}

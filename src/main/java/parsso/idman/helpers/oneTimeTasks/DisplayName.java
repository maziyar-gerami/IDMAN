package parsso.idman.helpers.oneTimeTasks;

import org.springframework.data.mongodb.core.MongoTemplate;

import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;

import java.util.List;

public class DisplayName {
    final MongoTemplate mongoTemplate;
    final UserRepo userRepo;

    DisplayName(MongoTemplate mongoTemplate, UserRepo userRepo) {
        this.mongoTemplate = mongoTemplate;
        this.userRepo = userRepo;
    }
    public void run(){
        int c = 0;
        char[] animationChars = new char[]{'|', '/', '-', '\\'};

        List<User> users = userRepo.retrieveUsersFull();
        for (User user: users) {
            if ((user.getDisplayName().contains("آقای") && !user.getDisplayName().contains("آقای "))
            || (user.getDisplayName().contains("خانم") && !user.getDisplayName().contains("خانم "))){
                String r = user.getDisplayName().substring(0,4) +" " +user.getDisplayName().substring(4);
                user.setDisplayName(r);
                user.getUsersExtraInfo().setDisplayName(r);

                userRepo.update("System",user.getUserId(),user);
            }
            int i = (++c * 100 / users.size());

            System.out.print("Processing displayName: " + i + "% " + animationChars[i % 4] + "\r");
        }

        System.out.println("Processing displayName: Done!");

    }
}

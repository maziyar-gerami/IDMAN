package parsso.idman.impls.skyroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.impls.skyroom.subclasses.LoginUrl;
import parsso.idman.impls.skyroom.subclasses.RandomPass;
import parsso.idman.impls.skyroom.subclasses.Register;
import parsso.idman.impls.skyroom.subclasses.UserRooms;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.SkyRoom;
import parsso.idman.models.users.User;
import parsso.idman.repos.SkyroomRepo;

import java.io.IOException;

@Service
public class SkyroomRepoImpl implements SkyroomRepo {
  @Autowired
  public SkyroomRepoImpl(UniformLogger uniformLogger, MongoTemplate mongoTemplate) {
    this.uniformLogger = uniformLogger;
    this.mongoTemplate = mongoTemplate;
  }

  UniformLogger uniformLogger;
  final MongoTemplate mongoTemplate;

  public SkyRoom run(User user) throws IOException {

    String RealName = user.getFirstName() + user.getLastName();
    String Classname = user.getFirstName().split("")[0] + user.getLastName().split("")[0]
        + (int) (Long.parseLong(user.getMobile()) % 937);
    int userId = new Register(mongoTemplate, uniformLogger).register(RealName, new RandomPass().make(8),
        user.getDisplayName());
    Boolean skyroomEnable = Boolean
        .parseBoolean(new Settings(mongoTemplate).retrieve(Variables.SKYROOM_ENABLE).getValue().toString());
    SkyRoom skyRoom;
    if (userId == 0) {
      int roomId = new UserRooms(mongoTemplate, uniformLogger).getRoomId(Classname);
      RealName = user.getFirstName() + " " + user.getLastName();

      skyRoom = new SkyRoom(skyroomEnable, user.getUsersExtraInfo().getRole(),
          new LoginUrl(mongoTemplate, uniformLogger).create(roomId, String.valueOf(userId), RealName),
          new UserRooms(mongoTemplate, uniformLogger).getGuestUrl(roomId));
      uniformLogger.info("System", new ReportMessage("Skyroom", "", "", "created", Variables.RESULT_SUCCESS,
          "for user \"" + user.get_id() + "\""));
      return skyRoom;
    }
    int roomId = new UserRooms(mongoTemplate, uniformLogger).create(Classname);
    new UserRooms(mongoTemplate, uniformLogger).add(userId, roomId);
    RealName = user.getFirstName() + " " + user.getLastName();
    try {
      return new SkyRoom(skyroomEnable, user.getUsersExtraInfo().getRole(),
          new LoginUrl(mongoTemplate, uniformLogger).create(roomId, String.valueOf(userId), RealName),
          new UserRooms(mongoTemplate, uniformLogger).getGuestUrl(roomId));
    } catch (Exception e) {
      return null;
    }
  }
}